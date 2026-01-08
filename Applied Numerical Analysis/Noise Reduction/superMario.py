import cv2
import numpy as np

def image_to_channels(image_path):
    # Load the image using OpenCV
    img = cv2.imread(image_path)
    
    if img is None:
        raise FileNotFoundError(f"Could not find image at {image_path}")

    # Convert BGR to RGB format
    img_rgb = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
    
    # Extract channels and convert to float64 for numerical precision
    # Each channel is a 2D matrix (f_ij)
    r_channel = img_rgb[:, :, 0].astype(np.float64) # Red channel
    g_channel = img_rgb[:, :, 1].astype(np.float64) # Green channel
    b_channel = img_rgb[:, :, 2].astype(np.float64) # Blue channel
    return r_channel, g_channel, b_channel

def channels_to_image(r_matrix, g_matrix, b_matrix):
    # Clip values to [0, 255] range and convert back to 8-bit integers
    # This is necessary because numerical methods can produce values slightly outside 0-255
    r_out = np.clip(r_matrix, 0, 255).astype(np.uint8)
    g_out = np.clip(g_matrix, 0, 255).astype(np.uint8)
    b_out = np.clip(b_matrix, 0, 255).astype(np.uint8)
    
    # Stack the channels along the 3rd dimension to create an RGB image
    merged_image = np.stack([r_out, g_out, b_out], axis=2)
    
    return merged_image

def calculate_mse(img_clean, img_denoised):
    clean = img_clean.astype(np.float64)
    denoised = img_denoised.astype(np.float64)
    mse = np.mean((clean - denoised) ** 2)
    return mse

def jacobi(f, u, a, b):
    u_new = u.copy() 
    height, width = f.shape
    
    for i in range(1, height - 1):
        for j in range(1, width - 1):
            # Υπολογίζουμε τη νέα τιμή στο u_new διαβάζοντας ΜΟΝΟ από το u_old
            #u_new i,j (k+1) = a * f[i, j] + b * (u_{i-1,j} + u_{i+1,j} + u_{i,j-1} + u_{i,j+1})
                            # sum_val =      u_{i-1,j} + u_{i+1,j} + u_{i,j-1} + u_{i,j+1}
            #Έτσι γλιτώνουμε υπολογιστικό χρόνο και πολυπλοκότητα και αποφεύγουμε την ανάγκη για αποθήκευση μεταβλητής sum_val
            u_new[i, j] = a * f[i, j] + b * (u[i-1, j] + u[i+1, j] + u[i, j-1] + u[i, j+1])
    
    return u_new

def gauss_seidel(f, u, a, b):
    # Τύπος Gauss-Seidel: Ενημερώνει το u "επί τόπου" (in-place)
    # Χρησιμοποιεί τις νέες τιμές μόλις υπολογιστούν
    # Στην Python οι loops είναι αργές, αλλά αυτός είναι ο κλασικός τρόπος υλοποίησης
    
    rows, cols = u.shape
    u_new = u.copy()

    for i in range(1, rows - 1):
        for j in range(1, cols - 1):
            # Χρησιμοποιούμε τις ΤΡΕΧΟΥΣΕΣ τιμές του u_new που μπορεί να έχουν ήδη ενημερωθεί
            # Τα u[i-1, j] και u[i, j-1] είναι οι "νέες" τιμές (όπως το sum1)
            # Τα u[i+1, j] και u[i, j+1] είναι οι "παλιές" τιμές (όπως το sum2)
            neighbors = u_new[i-1, j] + u_new[i+1, j] + u_new[i, j-1] + u_new[i, j+1]
            u_new[i, j] = a * f[i, j] + b * neighbors
            
    return u_new

def sor(f, u_curr, a, b, omega):
    
    # Συνδυασμός Gauss-Seidel με παράγοντα χαλάρωσης ω.
    rows, cols = u_curr.shape
    u_new = u_curr.copy()
    
    for i in range(1, rows - 1):
        for j in range(1, cols - 1):
            # 1. Υπολογίζω τους γείτονες 
            sum_neighbors = (u_new[i-1, j] + u_new[i+1, j] + 
                            u_new[i, j-1] + u_new[i, j+1])
            
            # 2. Υπολογίζω τι ΘΑ έβγαζε ο Gauss-Seidel 
            gs_part = a * f[i, j] + b * sum_neighbors
            
            # 3. Εφαρμόζω τον τύπο SOR πάνω σε αυτή την τιμή
            # Τύπος SOR: (1-ω)*old + ω*new
            u_new[i, j] = (1 - omega) * u_new[i, j] + omega * gs_part
            
    return u_new

# Κύριο πρόγραμμα
# ΑΡΧΙΚΟΠΟΙΗΣΗ: Φορτώνω την εικόνα με θόρυβο και την καθαρή εικόνα
image_path = 'mario_noisy.png'
clean_image_path = 'mario_clean.png'
clean_R, clean_G, clean_B = image_to_channels(clean_image_path)
full_clean = channels_to_image(clean_R, clean_G, clean_B)

results_table = [] 

# Παράμετροι
a = 0.08
b = 0.23
target_iterations = [2, 4, 6, 8, 10]

f_R, f_G, f_B = image_to_channels(image_path)

# Δημιουργία αντιγράφων για κάθε μέθοδο
uJacR, uJacG, uJacB = f_R.copy(), f_G.copy(), f_B.copy()
uGsR, uGsg, uGsb = f_R.copy(), f_G.copy(), f_B.copy()
uSorR, uSorG, uSorB = f_R.copy(), f_G.copy(), f_B.copy()

# Εύρεση της καλύτερης τιμής ω 
print("\n--> I am looking for the best omega value (experimental)...")

# List with values that we are trying experimentally (from 0 to 2)
candidate_omegas = [0.8, 1.0, 1.2, 1.4, 1.5, 1.6, 1.8]
best_mse = float('inf') # Ξεκινάμε με μεγάλο MSE
best_w = 1.5 # Default value

for w_val in candidate_omegas:
    # We are testing the Red
    temp_u = f_R.copy()
    
    for _ in range(4):
        temp_u = sor(f_R, temp_u, a, b, w_val)

    # Mesuring the MSE        
    current_mse = calculate_mse(clean_R, temp_u)
    print(f"   Test w={w_val}: MSE={current_mse:.2f}")
    
    # We are keeping the smallest MSE
    if current_mse < best_mse:
        best_mse = current_mse
        best_w = w_val

print(f"--> Best omega value: {best_w}")
w = best_w 


# Κύριος βρόχος επαναλήψεων
for k in range(1, 11):
    # Jacobi Method
    uJacR = jacobi(f_R, uJacR, a, b)
    uJacG = jacobi(f_G, uJacG, a, b) 
    uJacB = jacobi(f_B, uJacB, a, b)

    # Gauss-Seidel Method
    uGsR = gauss_seidel(f_R, uGsR, a, b)
    uGsg = gauss_seidel(f_G, uGsg, a, b)
    uGsb = gauss_seidel(f_B, uGsb, a, b)

    # SOR Method
    uSorR = sor(f_R, uSorR, a, b, w)
    uSorG = sor(f_G, uSorG, a, b, w)
    uSorB = sor(f_B, uSorB, a, b, w)

    # Έλεγχος για αποθήκευση στις επαναλήψεις 2, 4, 6, 8, 10
    if k in target_iterations:
        print(f"Saving iteration {k}")

        # Jacobi Method
        res_jacobi = channels_to_image(uJacR, uJacG, uJacB)
        cv2.imwrite(f'mario_jacobi_iter{k}.png', cv2.cvtColor(res_jacobi, cv2.COLOR_RGB2BGR))
        
        # Gauss-Seidel Method
        res_gs = channels_to_image(uGsR, uGsg, uGsb)
        cv2.imwrite(f'mario_gs_iter{k}.png', cv2.cvtColor(res_gs, cv2.COLOR_RGB2BGR))
        
        # SOR Method
        res_sor = channels_to_image(uSorR, uSorG, uSorB)
        cv2.imwrite(f'mario_sor_iter{k}.png', cv2.cvtColor(res_sor, cv2.COLOR_RGB2BGR))

        # Υπολογισμός MSE
        mse_j = calculate_mse(full_clean, res_jacobi)
        mse_g = calculate_mse(full_clean, res_gs)
        mse_s = calculate_mse(full_clean, res_sor)

        # Αποθήκευση στη λίστα για τον τελικό πίνακα
        results_table.append({
            'iter': k,
            'jacobi': mse_j,
            'gs': mse_g,
            'sor': mse_s
        })

# Εκτύπωση τελικού πίνακα αποτελεσμάτων        
print(f"{'iter':<10} | {'jacobi':<12} | {'gauss seidel':<12} | {'sor':<15}")
for row in results_table:
    print(f"{row['iter']:<10} | {row['jacobi']:<12.2f} | {row['gs']:<12.2f} | {row['sor']:<15.2f}")

