import os
import sys
import json

def get_all_files_in_directory(directory):
    all_files = []
    for root, dirs, files in os.walk(directory):
        for name in files:
            if name == '.DS_Store':
                pass
            else:
                all_files.append(os.path.join(root, name))
    return all_files

def generate_index_for_folder(path_to_folder):
	data = {}
	all_files = get_all_files_in_directory(path_to_folder)
	for item in all_files:
		parent_dir = os.path.dirname(item)
		current_filename = item.split('\\')[-1]
		parent_dir_name = parent_dir.split('\\')[-1]
		real_type = parent_dir_name
		if real_type in data:
			data[real_type] += 1
		else:
			data[real_type] = 1
	
	dataList = []
	for key in data:
		dict = {}
		dict['mimeType'] = key
		dict['count'] = data[key]
		#print(dict)
		dataList.append(dict)
		
		
	result = json.dumps(dataList, indent=4)
	print(result)

if __name__ == '__main__':
	if len(sys.argv) == 1:
		print('Usage : python fileCount.py <path_to_folder>')
	else:
		generate_index_for_folder(sys.argv[1])