import pandas as pd
import numpy as np
import csv
import requests

CSV_URL = 'https://srhdpeuwpubsa.blob.core.windows.net/whdh/COVID/WHO-COVID-19-global-daily-data.csv'

def Download_Csv(csv_url):

    with requests.Session() as s:
        download = s.get(csv_url)
        if download.status_code == 200:
            with open('../../Library/Application Support/JetBrains/PyCharmCE2023.1/scratches/data.csv', 'wb') as file:
                file.write(download.content)
                print('CSV extraction successful!')
        else:
            print('Failed')

    # with requests.Session() as s:
    #     download = s.get(CSV_URL)
    #     if download.status_code == 200:
    #         print('Successful!')
    #         with open('data.csv', 'wb') as file:
    #             file.write(download.content)
    #             print("CSV file downloaded!")


    df = pd.read_csv("../../Library/Application Support/JetBrains/PyCharmCE2023.1/scratches/data.csv")

    return df
# End function

def Data_Cleaning(df):
    # - Ensure text columns are strings
    df = df.astype({
        'Date_reported': 'datetime64[ns]',
        'Country_code':'str',
        'Country':'str',
        'WHO_region':'str'
    })
    # - Standardize date format
    df['Date_reported'] = pd.to_datetime(df['Date_reported'], format='mixed').dt.date

    # - deal with missing values
    nulls = df[df.isnull().any(axis=1)]
    df = df.fillna(0)

    # - check unique values in categorical variables
    df = df[df['Country_code'] != 'nan']
    # for item in {'Country_code', 'Country', 'WHO_region'}:
    #     print(df[item].unique())

    # - handle negative values
    num_cols = df.select_dtypes(include=[np.number]).columns
    df_neg = df[(df[num_cols] < 0).any(axis=1)].head(10)
    df[num_cols] = df[num_cols].mask(df[num_cols] < 0,df[num_cols].abs())
    
    return df
# End function

df = Download_Csv(CSV_URL)
df = Data_Cleaning(df)

df.to_csv('covid_data',index=False)