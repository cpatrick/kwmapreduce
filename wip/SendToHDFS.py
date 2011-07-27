import subprocess as sp
import sys
import os
import string

hadoopPath = '/home/cpatrick/Projects/MapReduce/hadoop-0.21.0/bin/hadoop'

def copyToHDFS(filename):
    """
    Send a file to hdfs using the hadoop fs put. This is done with the python
    subprocess module.
    """
    return sp.call([hadoopPath,'fs','-put',filename,
                    os.path.basename(filename)])

def main():
    """
    Iterate through a list of files and copy each to hdfs. When the copy is
    complete, the list itself is placed on hdfs.
    """
    listFile = open(sys.argv[1],'r')
    for line in listFile.readlines():
        copyToHDFS(string.strip(line))
    copyToHDFS(sys.argv[1])
    
if __name__ == '__main__':
    main()
