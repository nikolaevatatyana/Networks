package ru.nsu.ccfit.nikolaeva.copydetection;

import java.io.IOException;
import java.util.Scanner;

public class Main {

public static void main(String[] args) {
Sender sender = null;
Receiver receiver = null;
try {
sender = new Sender("hello");
receiver = new Receiver();
Scanner sc = new Scanner(System.in);

while(true) {
sender.iteration();
receiver.iteration();
if(System.in.available() != 0) {
String s = sc.nextLine();
if(s.equals("stop")) break;
}
}

} catch (IOException e) {
e.printStackTrace();
} finally {
if(sender != null)sender.close();
if(receiver != null)receiver.close();
}
}

} 
