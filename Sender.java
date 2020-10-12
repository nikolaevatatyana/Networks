package ru.nsu.ccfit.nikolaeva.copydetection;

import java.io.IOException;
import java.net.*;

public class Sender {
private DatagramSocket socket;
private InetAddress group;
private byte[]buf;

long start;
long prevTime;
long deltaTime = 0;
long delay = 3000;

public Sender(String multicastMessage) throws SocketException, UnknownHostException {
socket = new DatagramSocket();
group = InetAddress.getByName("230.0.0.0");

buf = multicastMessage.getBytes();

start = System.currentTimeMillis();
prevTime = System.currentTimeMillis();
}

public void close () {
socket.close();
}

public void iteration() throws IOException {
deltaTime = System.currentTimeMillis() - prevTime;
prevTime = System.currentTimeMillis();

if((delay -= deltaTime) < 0) {
delay = 3000;
DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
socket.send(packet);
}
}
}
