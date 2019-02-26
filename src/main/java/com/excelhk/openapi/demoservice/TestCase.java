package com.excelhk.openapi.demoservice;

public class TestCase {

    public static void main(String [] args){
        int i = -1;
        System.out.println(++i);
        System.out.println(i++);



       /* String localInpath = "ftp-inbound/";
        String fileName = "prod.Deposits.D1.578219702174720.tmp";
        //fileName = localInpath + fileName;
        System.out.println("backup fileName: " + fileName );
        File fileIn = new File(localInpath + fileName);
        File fileInBak = new File(fileName.replace(".tmp" , ".csv" ));
        if(fileIn.renameTo(fileInBak)){
            System.out.println("backup file successfully" );
        }else{
            System.out.println("backup file failed" );
        }*/
    }
}
