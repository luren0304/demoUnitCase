package com.excelhk.openapi.demoservice;

import java.io.File;

public class TestCase {

    public static void main(String [] args){
        String path1 = "ftp-inbound/prod.Deposits.D1.577182451549184.csv";
        String path2 = "ftp-inbound/bak/prod.Deposits.D1.577182451549184.csv";
        File file1 = new File(path1);
        File file2 = new File(path2);

        System.out.println(file1.renameTo(file2));
    }
}
