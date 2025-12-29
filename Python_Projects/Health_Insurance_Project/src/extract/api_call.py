import requests
import pandas as pd

url = 'https://data.cms.gov/data.json'
# data_url = 'https://data.cms.gov/data-api/v1/dataset/44060663-47d8-4ced-a115-b53b4c270acb/data'
title = 'Hospital Provider Cost Report'
time_period = 'latest'



response = requests.get(url)
if response.ok:
    response = response.json()
    dataset = response['dataset']
    for set in dataset:
        if title == set['title']:
            for distro in set['distribution']:
                if 'format' in distro.keys() and 'description' in distro.keys():
                    if distro['format'] == 'API' and distro['description'] == 'latest':
                        latest_distro = distro['accessURL']
                        print(f"API link for {title} from {time_period} is {distro['accessURL']}")
stats_endpoint = latest_distro + '/stats'
stats_response = requests.get(stats_endpoint)
stats_response = stats_response.json()
total_rows = stats_response['total_rows']
print(f'Total rows: {total_rows}')

i = 0
while i < total_rows:
    size = 5000
    offset_url = f'{latest_distro}?size={size}&offset={i}'

    offset = i
    offset_response = requests.get(offset_url)
    print(f'Made request for {size} results at offset {i}')
    #save data
    data = offset_response.json()
    i = i+size