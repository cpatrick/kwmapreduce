package com.kitware.kwmapreduce;

import java.io.IOException;
import java.io.ByteArrayOutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;

public class DirToSequenceFile {

  /**
   * reads all files in the inputDir on the inputFS, for each file found, will
   * combine them into a single SequenceFile on the outputFS with the name
   * sequenceFile, writing each (key,value) pair to be 
   * (filename_i, filename_i_bytes). 
   */
  public static void writeDirFilesToHadoopSequenceFile(FileSystem inputFS, FileSystem outputFS, Path inputDir, Path sequenceFile, Configuration conf) throws IOException
    {
      FileStatus[] inputFiles = inputFS.listStatus(inputDir);
      SequenceFile.Writer writer = SequenceFile.createWriter(outputFS, conf, sequenceFile, Text.class, BytesWritable.class);

      for (int i = 0; i < inputFiles.length; i++) 
        {
        System.out.println("Now writing:" + inputFiles[i].getPath().getName());
        FSDataInputStream in = inputFS.open(inputFiles[i].getPath());

        byte buffer[] = new byte[1024 * 1024];
        
        // write the current file into the bout 
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        int bytesRead = 0;
        while( (bytesRead = in.read(buffer, 0, buffer.length)) >= 0 ) {
	  bout.write(buffer);
        }
        in.close();
        // the key is the filename
        Text filename = new Text(inputFiles[i].getPath().getName());
        // the value is the bytearray of the input file
        writer.append(filename,new BytesWritable(bout.toByteArray()));
      }
      writer.close();
    }

  /**
   * main method will take in two args: the inputDirectory and
   * the output file name, will create a SequenceFile in HDFS
   * with the keys being every filename in the inputDirectory,
   * and the values being the actual bytes of the corresponding file.
   */ 
  public static void main(String[] args) throws IOException
    {
    Configuration conf = new Configuration();
    FileSystem hdfs = FileSystem.get(conf);
    FileSystem local = FileSystem.getLocal(conf);
 
    Path inputDir = new Path(args[0]);
    Path hdfsFile = new Path(args[1]);
     
    writeDirFilesToHadoopSequenceFile(local, hdfs, inputDir, hdfsFile, conf);
    }

}
