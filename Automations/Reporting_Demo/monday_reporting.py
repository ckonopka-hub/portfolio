import pandas as pd
import matplotlib.pyplot as plt
import matplotlib.cm as cm
from datetime import datetime
import os
from matplotlib.ticker import MaxNLocator, FormatStrFormatter
from openpyxl import load_workbook
from openpyxl.drawing.image import Image as XLImage

# Data Cleaning
def cleaning(df: pd.DataFrame):
    import pandas as pd
    """
    :param df: pd.DataFrame
        Dataset to be cleaned.
    :returns: pd.DataFrame
        Cleaned dataset.
    """

    # clean column names
    df.columns = df.columns.str.strip()

    # trim all string columns
    df = df.apply(
        lambda col: col.str.strip() if col.dtype == "object" else col
    )
    # print(df.columns)

    # address null values
#     print("Null Values: " + str(df.isna().sum()))

    # fill nulls that can have "Unknown" value
    df[['assignee','issuer_email']] = df[['assignee','issuer_email']].fillna("Unknown")
    # look at rest of nulls directly
    # with pd.option_context(
    #         'display.max_rows', None,
    #         'display.max_columns', None,
    #         'display.max_colwidth', None,
    #         'display.width', None
    # ):
    #     print(df[df.isna().any(axis=1)])
    # fill specific priority nulls
    df.loc[df[('ticket_id')] == 'TCK-20010','priority'] = 'High'
    df.loc[df[('ticket_id')] == 'TCK-20025','priority'] = 'Low'

    # fill specific website nulls
    df.loc[df[('ticket_id')] == 'TCK-20014','website_type'] = 'Corporate Website'
    df.loc[df[('ticket_id')] == 'TCK-20032','website_type'] = 'Learning Management System'
#     print('Null Values after cleaning: ' + str(df.isna().sum()))

    # fix priority column
#     print(df['priority'].value_counts(dropna=False))
    df['priority'] = df['priority'].str.strip().str.lower()
#     print(df['priority'].value_counts(dropna=False))

    # fix status column
#     print(df['status'].value_counts(dropna=False))
    df['status'] = df['status'].str.strip().str.lower()
#     print(df['status'].value_counts(dropna=False))

    # address duplicate values
#     print("Duplicate rows: " + str(df.duplicated().sum()))
    df = df.drop_duplicates()
#     print("Duplicate rows after cleaning: " + str(df.duplicated().sum()))

    return df
# Filtering
def filtering_critical(df: pd.DataFrame):

    filtered_df = df[(df['priority'] == 'critical') & (df['status'] != 'closed')]
    # print(filtered_df[['ticket_id','status','priority']].head())
    return df

# Visualization
def open_tickets_by_client(
        df: pd.DataFrame,
        title: str = 'Open Tickets by Status (colored by Client)',
        filename: str = 'tickets_and_clients_by_status.png',
        full_path: str = '/Users/macowner/Downloads/tickets_and_clients_by_status.png'
):

    # bar chart of number of tickets in each status, colored by client
    print(df['status'].unique())
    status_order = [
        'new',
        'in progress',
        'waiting on client',
        'blocked'
    ]
    df['status'] = pd.Categorical(df['status'], categories=status_order,ordered=True)
    print(df.columns)
    pivot = (
        df.pivot_table(index='status', columns='client_name', values='ticket_id',aggfunc='count',fill_value=0).sort_index()
    )
    ax = pivot.plot(kind='bar', figsize = (12,7), stacked=True, edgecolor='none')

    # styling
    ax.set_title(title, fontsize = 18)
    ax.set_xlabel('Status', fontsize = 15)
    ax.set_ylabel('Number of tickets', fontsize = 15)
    ax.set_xticklabels(ax.get_xticklabels(), rotation=45, ha='right', fontsize = 12)

    ax.yaxis.set_major_locator(MaxNLocator(integer=True))
    ax.yaxis.set_major_formatter(FormatStrFormatter('%d'))

    # place legend outside chart
    ax.legend(title='Client', bbox_to_anchor=(1.02, 1), loc='upper left', borderaxespad=0.)
    ax.grid(axis='y', linestyle=":", alpha=0.5)

    plt.tight_layout()

    plt.savefig(filename, dpi=600)
    plt.show()
    plt.close()
# end of function

# create filename
today = datetime.today().strftime('%Y-%m-%d')
filename = f'ticket_report_{today}.xlsx'
pathname = '/Users/macowner/Downloads/' # adjust as needed
full_path = os.path.join(pathname, filename)

# run functions to generate dataframes and chart
df = pd.read_csv('/Users/macowner/PycharmProjects/automation/tech_tickets_with_issues.csv')
df_cleaned = cleaning(df)
critical_df = filtering_critical(df_cleaned)
open_tickets_by_client(critical_df)

# write dataframes to excel file
with pd.ExcelWriter(full_path) as writer:
    df.to_excel(writer,sheet_name='Raw_Data')
    df_cleaned.to_excel(writer,sheet_name='Cleaned_Data')
    critical_df.to_excel(writer,sheet_name='Critical_Tickets')

# load excel workbook for chart
wb = load_workbook(full_path)
ws = wb.create_sheet('Tickets_by_Status')

# create image object
chart = XLImage()

# insert chart into sheet
ws.add_image(chart,"B2",)

# save excel file
wb.save(full_path)
