/*
 *
 * Copyright (c) 2017-2022 by Ovfintech (Wuhan) Technology Co., Ltd.
 * All rights reserved.
 *
 */

package com.fruit.portal.utils;

import org.apache.commons.io.IOUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.net.URI;
import java.util.Deque;
import java.util.Enumeration;
import java.util.LinkedList;

/**
 * Description:
 * <p/>
 * Create Author  : terry
 * Create Date    : 2017-05-20
 * Project        : message-biz
 * File Name      : ZipUtil.java
 */
public class ZipUtil
{
    public static void zip(File directory, File zipfile) throws IOException
    {
        URI base = directory.toURI();
        Deque<File> queue = new LinkedList<File>();
        queue.push(directory);
        OutputStream out = new FileOutputStream(zipfile);
        ZipOutputStream zout = null;
        try
        {
            zout = new ZipOutputStream(out);
            while (!queue.isEmpty())
            {
                directory = queue.pop();
                for (File kid : directory.listFiles())
                {
                    String name = base.relativize(kid.toURI()).getPath();
                    if (kid.isDirectory())
                    {
                        queue.push(kid);
                        name = name.endsWith("/") ? name : name + "/";
                        zout.putNextEntry(new ZipEntry(name));
                    }
                    else
                    {
                        zout.putNextEntry(new ZipEntry(name));
                        copy(kid, zout);
                        zout.closeEntry();
                    }
                }
            }
        }
        finally
        {
            IOUtils.closeQuietly(zout);
            IOUtils.closeQuietly(out);
        }
    }

    public static void unzip(File zipfile, File directory) throws IOException
    {
        ZipFile zfile = new ZipFile(zipfile);
        Enumeration<? extends ZipEntry> entries = zfile.getEntries();
        while (entries.hasMoreElements())
        {
            ZipEntry entry = entries.nextElement();
            File file = new File(directory, entry.getName());
            if (entry.isDirectory())
            {
                file.mkdirs();
            }
            else
            {
                file.getParentFile().mkdirs();
                InputStream in = zfile.getInputStream(entry);
                try
                {
                    copy(in, file);
                }
                finally
                {
                    in.close();
                }
            }
        }
    }

    private static void copy(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        while (true)
        {
            int readCount = in.read(buffer);
            if (readCount < 0)
            {
                break;
            }
            out.write(buffer, 0, readCount);
        }
    }

    private static void copy(File file, OutputStream out) throws IOException
    {
        InputStream in = new FileInputStream(file);
        try
        {
            copy(in, out);
        }
        finally
        {
            in.close();
        }
    }

    private static void copy(InputStream in, File file) throws IOException
    {
        OutputStream out = new FileOutputStream(file);
        try
        {
            copy(in, out);
        }
        finally
        {
            out.close();
        }
    }
}
