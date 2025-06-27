
"""""
PROGRAM INFORMATION:

Last Modified Date: 2025-06-27
By: Carson Konopka
Purpose: Script to automatically organize, archive, and delete files in a given folder

"""""

"""
Changelog:

2025-06-01 - Initial version finished: cleaning, organizing, archiving, and deleting
2025-06-03 - Created crontab for running script on Wednesdays at 7am
2025-06-08 - crontab did not run, troubleshooting
           - solved file permission issue, crontab running successfully
           - set to run organization function at 7am every Monday
2025-06-19 - fixed archiving function with subprocess module and datetime manipulation
2025-06-21 - tested deletion function successfully
           - added functions for file access date retrieval and deleting empty folders
           - fixed handling of files without access dates
2025-06-25 - touched up full script, now ready to post
"""

import os
import shutil
import datetime as dt
from datetime import datetime, timezone
import subprocess

""" Utility functions """
# remove any empty folders within a folder/directory
def delete_empty_folders(folder):
    for root, dirs, files in os.walk(folder, topdown=False):
        for dir in dirs:
            # if directory does not have any files (will return empty list that is evaluated as False)
            if not os.listdir(os.path.join(root, dir)):
                try:
                    print(f"Deleting empty folder: {dir}")
                    # remove directory (will not work if it's not empty)
                    os.rmdir(os.path.join(root, dir))
                except(OSError):
                    print("Could not delete folder: {}".format(os.path.join(root, dir)))
# end of function

# Returns date a file was last opened by an app or user
def get_last_used_date(file_path):
    try:
        # accesses metadata of file
        result = subprocess.run(['mdls', file_path],stdout=subprocess.PIPE, stderr=subprocess.DEVNULL, text=True)
        for line in result.stdout.splitlines():
            if line.startswith('kMDItemLastUsedDate '):
                date_str = line.split('= ')[1].strip()
                date = datetime.strptime(date_str,'%Y-%m-%d %H:%M:%S %z')
                date = date.replace(tzinfo=None)
                return date
        # if there is no access data - set to be archived
        return datetime.strptime("2024-07-17 00:00:00", '%Y-%m-%d %H:%M:%S')
    except Exception as e:
        print(f'Error reading metadata: {e}')
# end of function

# - Name cleaning: replace spaces with underscores
def filename_cleaning(folder):
    for root, dirs, files in os.walk(folder):
        for file in files:
            new_name = file.replace(' ', '_')
            os.rename(os.path.join(root,file),os.path.join(root,new_name))
# end of function
""" End of Utility functions """

# - Place files into folders based on extension/file type
def organize_folder(folder):
    print("Organizing folder")
    for root, dirs, files in os.walk(folder): # traverse entire folder
        for file in files: # visit each file
            file_extension = file.split('.')[-1].upper() if '.' in file else 'MISCELLANEOUS'

            # create folder for extension if there isn't one already
            folder_path = os.path.join(folder, file_extension)
            os.makedirs(folder_path, exist_ok=True)

            # set source and destination paths
            source_path = os.path.join(folder, file)
            destination_path = os.path.join(folder_path, file)

            # check if file is already in correct folder
            if os.path.exists(destination_path): continue

            # move file to extension folder
            try:
                shutil.move(source_path, destination_path)
            except(OSError):
                print("Could not move file {} to {}".format(file, destination_path))

    print('Finished organizing folder')

# end of function

# finds all files within a folder (or any nested folders) and moves them to the main folder
def folder_deorganizer(folder):
    for root, dirs, files in os.walk(folder, topdown=False):
        # find all files
        for file in files:
            # set source and destination paths
            src = os.path.join(root, file)
            dest = os.path.join(folder, file)
            try:
                # Edge case: if folder has same name as file
                if root.rsplit('/', 1)[1] == file:
                    # check that file has no extension
                    print("File and directory have same name, changing file name")
                    if '.' not in file or file.rsplit('.', 1)[1] == '':
                        # add extension
                        dest = f'{src}.txt'
                        os.rename(src, dest)
                        file = dest.rsplit('/', 1)[1]
                        print("Renamed file: {}".format(dest))
                        # move file
                        shutil.move(src, os.path.join(folder, file))
                        print("File moved successfully.")
                    continue
                # move file out of folder
                shutil.move(src, dest)
            except(shutil.Error):
                print("Could not move file: {}".format(src))
    # delete any empty folders created from moving files
    delete_empty_folders(folder)
# end of function

# - Archive files that haven't been opened or modified in 90 days
def archive_files(folder):
    print('Archiving files...')
    # initialize variables
    current_date = datetime.now()

    # create folder if it doesn't exist already
    archive_path = os.path.join(folder, 'Archives')
    os.makedirs(archive_path, exist_ok=True)

    # iterate through all files
    for root, dirs, files in os.walk(folder):
        if 'Archives' in root: continue # skip files in archive folder

        for file in files:
            # find last access date
            access_time = get_last_used_date(os.path.join(root, file))

            # get difference between current date and when it was accessed
            access_difference = current_date - access_time

            # evaluate if file has not been accessed/modified in 3 months
            if access_difference > dt.timedelta(days=90): # if file has not been accessed in 3 months
                # move file to archive folder
                src_path = os.path.join(root, file)
                dst_path = os.path.join(archive_path, file)
                try:
                    # print statement to use for tracking
                    # print('Archiving file: {}, {} days old'.format(file, access_difference.days))
                    # move file into archive folder
                    shutil.move(src_path, dst_path)
                except(Error):
                    print('Failed to move file: {}'.format(file))
    # remove any empty folders after files have been archived
    delete_empty_folders(folder)
    print('Finished archiving files.')
# end of function

# Delete archived files once they haven't been accessed in 1 year
def delete_files(folder):
    # search for an archive folder - if not there, send error message
    if 'Archives' not in os.listdir(folder):
        print("ERROR: Cannot delete files - no Archive folder present.")
    else:
        # set variables
        archive_location = os.path.join(folder, 'Archives')
        trash_location = '/Users/macowner/.Trash/' # configured for Mac computers
        current_date = datetime.now()

        for root, dirs, files in os.walk(archive_location):
            for file in files:
                access_date = get_last_used_date(os.path.join(root, file))
                access_difference = current_date - access_date

                # if file has not been accessed in 1 year or more, send to trash
                if access_difference > dt.timedelta(weeks = 52):
                    try:
                        print('Sending file to trash: {}, {} days old'.format(file, access_difference.days))
                        shutil.move(os.path.join(root, file), os.path.join(trash_location, file))
                    except(Error):
                        print('Failed to send file to trash: {}'.format(file))
        # delete any empty folders created from moving files
        delete_empty_folders(archive_location)
# end of function


# - FUNCTION CALLS: will run every week on specific directory
organize_folder('/Users/macowner/Downloads')
archive_files('/Users/macowner/Downloads')
delete_files('/Users/macowner/Downloads')

