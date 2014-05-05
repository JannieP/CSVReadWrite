package net.c0nan.csv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CSVReader
{

  private Reader reader = null;
  private BufferedReader bufferedReader = null;
  private List<String> buffer = new ArrayList<String>();
  private StringBuilder line = new StringBuilder();
  
  private int columnCount = 0;

  public boolean bufferData = false;

  public CSVReader(String inputFile, int columnCount) throws FileNotFoundException
  {
    this(new FileReader(inputFile), columnCount);
  }

  public CSVReader(Reader reader, int columnCount)
  {
    super();
    this.reader = reader;
    this.columnCount = columnCount;
    bufferedReader = new BufferedReader(reader);
  }

  public void readAll() throws IOException
  {
    readLine();
    while (!getLine().isEmpty())
    {
      readLine();
    }
  }

  public void readLine() throws IOException
  {

    boolean addedQuote = false;

    line = new StringBuilder();

    String tmpline = "";

    String[] arrCSV = tmpline.split(CSVUtils.getSplitRegex(), -1);

    while (arrCSV.length < columnCount)
    {

      tmpline = bufferedReader.readLine();

      if ( addedQuote )
      {
        line.setLength(line.length() - 1);
        addedQuote = false;
      }
      if ( tmpline != null )
      {
        line.append(tmpline);
      }

      if ( line.length() == 0 )
        break;

      if ( (tmpline != null && !tmpline.isEmpty()) && tmpline.charAt(tmpline.length() - 1) != '"' )
      {
        line.append("\"");
        addedQuote = true;
      }

      arrCSV = line.toString().split(CSVUtils.getSplitRegex(), -1);

    }

    if ( line.length() > 0 && bufferData )
    {
      buffer.add(getLine());
    }

  }

  public String getLine()
  {
    return line.toString();
  }

  public String[] getLineAsArray()
  {
    return line.toString().split(CSVUtils.getSplitRegex(), -1);
  }

  public List<String> getBuffer()
  {
    return buffer;
  }

  

  public void close() throws IOException
  {
    if ( bufferedReader != null )
    {
      bufferedReader.close();
    }
    if ( reader != null )
    {
      reader.close();
    }
  }

  @Override
  protected void finalize() throws Throwable
  {
    super.finalize();
    close();
  }

}
