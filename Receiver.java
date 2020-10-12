package ru.nsu.ccfit.nikolaeva.copydetection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class Receiver {
protected MulticastSocket socket = null;
protected byte[]buf = new byte[256];

private ArrayList<client> clients = new ArrayList<client>();

InetAddress group;

long prevTime;
long deltaTime;
long timer;

public Receiver() throws IOException {
socket = new MulticastSocket(4446);
group = InetAddress.getByName("230.0.0.0");
socket.joinGroup(group);

prevTime = System.currentTimeMillis();
deltaTime = 0;
timer = 0;
}

public void iteration() throws IOException {

deltaTime = System.currentTimeMillis() - prevTime;
prevTime = System.currentTimeMillis();

for (client c : clients) {
c.increment_time(deltaTime);
if(c.getTime_millis() > 10000) c.isOnline = false;
}

DatagramPacket packet = new DatagramPacket(buf, buf.length);
try{
socket.setSoTimeout(1000);
socket.receive(packet);


String received = new String(packet.getData(), 0, packet.getLength());

boolean client_found = false;

for (client c : clients) {
if(c.getName().equals(packet.getSocketAddress().toString())) {
client_found = true;
c.reset_timer();
c.isOnline = true;
break;
}
}

if(!client_found) {
clients.add(new client(packet.getSocketAddress().toString()));
}

} catch (SocketTimeoutException e){}

//System.out.println(packet.getSocketAddress());

timer -= deltaTime;

if(timer < 0){
timer = 3000;

System.out.print("\033[H\033[2J");
System.out.flush();

for (client c: clients) {
if(c.isOnline) System.out.println(c.name);
}
}
}

public void close() {
try {
socket.leaveGroup(group);
} catch (IOException e) {
e.printStackTrace();
} finally {
socket.close();
}
}

private class client {
private String name;
private long time_millis = 0;

private boolean isOnline;

public String getName() {return name;}
public long getTime_millis() {return time_millis;}

public client(String name) {
this.name = name;
isOnline = true;
}

public void increment_time(long delta_time_millis) {
time_millis += delta_time_millis;
if(time_millis < 0) time_millis = 0;
}

public void reset_timer() {
time_millis = 0;
}
}
}
