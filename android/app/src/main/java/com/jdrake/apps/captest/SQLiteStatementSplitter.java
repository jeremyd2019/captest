package com.jdrake.apps.captest;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SQLiteStatementSplitter implements Iterable<String>, Closeable
{
	private final Reader fReader;

	public SQLiteStatementSplitter(InputStream is)
	{
		try
		{
			fReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			// You damn well better support UTF-8
			throw new RuntimeException(e);
		}
	}

	@Override
	public Iterator<String> iterator()
	{
		return new Iterator<String>()
		{
			StringBuilder fBuilder = new StringBuilder();

			@Override
			public boolean hasNext()
			{
				if (fBuilder.length() > 0)
					return true;
				String tmp;
				do
				{
					tmp = null;
					fBuilder.setLength(0);
					try
					{
						int ch;
						boolean semiFound = false;
						while (!semiFound && (ch = fReader.read()) != -1)
						{
							fBuilder.append((char)ch);
							switch (ch)
							{
							case '`':
							case '\'':
							case '"':
							{
								int delim = ch;
								while ((ch = fReader.read()) != -1)
								{
									fBuilder.append((char)ch);
									if (ch == delim)
									{
										fReader.mark(1);
										if (fReader.read() == delim)
										{
											fBuilder.append((char)ch);
										}
										else
										{
											fReader.reset();
											break;
										}
									}
								}
								break;
							}
							case '[':
								while ((ch = fReader.read()) != -1)
								{
									fBuilder.append((char)ch);
									if (ch == ']')
										break;
								}
								break;
							case '-':
								fReader.mark(1);
								if (fReader.read() != '-')
								{
									fReader.reset();
								}
								else
								{
									fBuilder.append((char)ch);
									while ((ch = fReader.read()) != -1)
									{
										fBuilder.append((char)ch);
										if (ch == '\n')
											break;
									}
								}
								break;
							case '/':
								fReader.mark(1);
								if (fReader.read() != '*')
								{
									fReader.reset();
								}
								else
								{
									fBuilder.append('*');
									while ((ch = fReader.read()) != -1)
									{
										fBuilder.append((char)ch);
										if (ch == '*')
										{
											fReader.mark(1);
											if (fReader.read() == '/')
											{
												fBuilder.append('/');
												break;
											}
											fReader.reset();
										}
									}
								}
								break;
							case ';':
								semiFound = true;
								break;
							}
						}
					}
					catch (IOException e)
					{
						throw new RuntimeException(e);
					}
				} while (fBuilder.length() > 0 && (tmp = fBuilder.toString().trim()).length() == 0);
				if (tmp != null)
				{
					fBuilder.setLength(0);
					fBuilder.append(tmp);
				}
				return fBuilder.length() > 0;
			}

			@Override
			public String next()
			{
				if (fBuilder.length() <= 0 && !hasNext())
					throw new NoSuchElementException();
				String ret = fBuilder.toString();
				fBuilder.setLength(0);
				return ret;
			}

			@Override
			public void remove()
			{
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public void close() throws IOException
	{
		fReader.close();
	}
}