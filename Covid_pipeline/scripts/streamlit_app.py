import pandas as pd
import streamlit as st
import altair as alt
import plotly.express as px

#5) - Make simple dashboard with Streamlit + host on Streamlit Cloud
#   - - Streamlit documentation: https://docs.streamlit.io/
def Dashboard_Config():
    st.set_page_config(
        page_title='Covid Data Dashboard',
        layout='wide',
        initial_sidebar_state= 'expanded')

def Make_Choropleth(input_df, input_id, input_column, input_color_theme):
    choropleth = px.choropleth(input_df, locations = input_id ,color = input_column, locationmode = 'country names',
                               color_continuous_scale=input_color_theme,
                               labels={'Cumulative_deaths':'Cumulative Deaths'})
    choropleth.update_layout(
        template='plotly_dark',
        plot_bgcolor='rgba(0,0,0,0)',
        paper_bgcolor='rgba(0,0,0,0)',
        margin=dict(l=0,r=0,t=0,b=0),
        height=350
    )
    return choropleth

def Make_Line_Chart(df, x, y):
    st.line_chart(df, x, y)


#6) - Use Amazon Airflow DAG to automate daily ingestion + loading into Postgres

#7) - FUNCTION EXECUTION
df = pd.read_csv('Covid_pipeline/data/covid_data')
df['Date_reported'] = pd.to_datetime(df['Date_reported'])
df['Month_year'] = df['Date_reported'].dt.to_period('M').dt.to_timestamp()

Dashboard_Config()

with st.sidebar:
    st.title('Covid Dashboard')

    dropdown_dict = {
        'Cumulative cases':'Cumulative_cases',
        'Cumulative deaths':'Cumulative_deaths',
        'Average rate (per month) of new cases':'avg_cases',
        'Average rate (per month) of deaths':'avg_deaths'}
    selected_label = st.selectbox('Select a measurement', list(dropdown_dict.keys()))
    selected_value = dropdown_dict[selected_label]



# - For cumulative deaths
df_choro_1 = df.groupby('Country').Cumulative_deaths.max()
df_line_1 = df.groupby('Date_reported').Cumulative_deaths.sum()

# - For cumulative cases
df_choro_2 = df.groupby('Country').Cumulative_cases.max()
df_line_2 = df.groupby('Date_reported').Cumulative_cases.sum()

# - For average rate of new cases
df_choro_3 = df.groupby('Country').New_cases.mean()
df_line_3 = df.groupby('Month_year').New_cases.sum()

# - For average rate of deaths
df_choro_4 = df.groupby('Country').New_deaths.mean()
df_line_4 = df.groupby('Month_year').New_deaths.sum()
print(df_line_4.head(20))

# - change dashboard to selected measurement dataframes
if selected_value == 'Cumulative_cases':
    df_choro = df_choro_1
    df_line = df_line_1
elif selected_value == 'Cumulative_deaths':
    df_choro = df_choro_2
    df_line = df_line_2
elif selected_value == 'avg_cases':
    df_choro = df_choro_3
    df_line = df_line_3
else:
    df_choro = df_choro_4
    df_line = df_line_4


df_choro = df_choro.reset_index()
df_line = df_line.reset_index()


# col = st.rows((5, 2), gap = 'medium')
#with col[0]:
choropleth = Make_Choropleth(df_choro, df_choro.columns[0], df_choro.columns[1], 'Viridis')
st.plotly_chart(choropleth, use_container_width = True)
#with col[1]:
st.line_chart(df_line, x = df_line.columns[0], y = df_line.columns[1])



# streamlit run '/Users/macowner/Library/Application Support/JetBrains/PyCharmCE2023.1/scratches/dashboard_creation.py'
