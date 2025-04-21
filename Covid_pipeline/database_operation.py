import psycopg2 as ps # I FINALLY GOT ITTTT
import psycopg2.extras as extras
from sqlalchemy import create_engine

#3) - Model in PostgreSQL (database) - downloaded with Homebrew

    # SQL statement used to create Covid_data table
def Covid_Table_Statement():
    table_creation_stmt = '''create table Covid_Data(date_reported DATE\
                             country_code varchar(10)\
                             country varchar(100)\
                             who_region varchar(10)\
                             new_cases float\
                             new_deaths float\
                             cumulative_cases int\
                             cumulative_deaths int'''

    # - Sends dataframe to postgres database
# NEED TO: update connection object for this function to use sqlalchemy
def Upload_Dataframe(conn, df, table_name):
    df.to_sql(table_name, con=conn, if_exists='replace',index=False)

    # tuples = [tuple(x) for x in df.to_numpy()]
    #
    # cols = ','.join(list(df.columns))
    # # SQL query
    # query = "insert into %s (%s) values %%s" % (table, cols)
    # cursor = conn.cursor()
    # try:
    #     extras.execute_values(cursor, query, tuples)
    #     conn.commit()
    # except (Exception, ps.DatabaseError) as error:
    #     print('Error: %s' % error)
    #     conn.rollback()
    #     cursor.close()
    #     return 1
    # print("The dataframe is inserted")
    # cursor.close()

#4) - Pull data from Postgres with SQL statement
def Data_Pull(conn, sql_statement):
    df = pd.read_sql_query(
        sql_statement, con=conn)
    return df

conn = create_engine('postgresql+psycopg2://macowner:\@localhost/postgres')
# URL - postgresql://macowner:@localhost:5432/postgres
#df = Download_Csv(CSV_URL)

#Upload_Dataframe(conn, df, 'Covid_Data') # - Inserts data into Postgres

sql = 'select * from Covid_data'
#df_2 = Data_Pull(conn, sql)
#print(df_2.head(10))

sql_1 = 'select country, max(cumulative_deaths) as cumulative_deaths from Covid_data group by country'
sql_2 = 'select date_reported, sum(cumulative_deaths) as cumulative_deaths from Covid_data group by date_reported'
# df_2 = Data_Pull(conn, sql_2)
# df_2 = df_2.sort_values(by='date_reported')

sql_3 = 'drop table Covid_data'
