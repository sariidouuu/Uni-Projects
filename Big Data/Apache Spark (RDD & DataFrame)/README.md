# Customer Churn Analysis in Telecommunications

This project focuses on predicting customer churn for a telecommunications provider using **Apache Spark**, leveraging its capability to process large-scale datasets efficiently.

## 🛠 Technologies & Tools
* **Apache Spark:** Core framework for large-scale data processing.
* **PySpark:** Python API for Spark, used for implementing machine learning pipelines.
* **Spark MLlib:** Used for building and evaluating predictive classification models.
* **Jupyter Notebooks:** For interactive data analysis and visualization.

## 📊 Project Overview
The analysis follows a structured Big Data workflow:
1. **Exploratory Data Analysis (EDA):** Identifying patterns and correlations between customer features and churn rates.
2. **Data Preprocessing:** Handling imbalanced data, feature scaling, and string indexing for categorical variables using Spark Pipelines.
3. **Model Development:** Implementing classification algorithms such as Logistic Regression, Random Forest, or Gradient-Boosted Trees (GBT).
4. **Evaluation:** Assessing model performance using metrics like Area Under ROC (AUC), Precision, and Recall.

## 📁 File Structure
* `Churn_Analysis_Spark.ipynb`: The main Spark notebook containing the end-to-end analysis.
* `telecom_churn_data.csv`: The dataset containing customer demographics, account information, and usage patterns.
* `Report.pdf`: Comprehensive documentation of the methodology and results (in Greek).

---
*This project was implemented as part of the **Big Data** course at the Department of Applied Informatics, University of Macedonia.*
