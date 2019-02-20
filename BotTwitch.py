### Ver 1.0 
### Ref. streaming.ChatClient.java in FightingICE
### If you use this, you must check the option ("--LiveStreaming --chat") in FightingICE.
import socket
#from socket import *
import datetime
import re
import os
### Options (Don't edit)
SERVER = "irc.twitch.tv"  # server
PORT = 6667 # port
PORT_JAVA = 6000
### Options (Edit this)
PASS = "oauth:u5tpkrtf75it3ufmg3eh1ftu1x1bry"  # bot password can be found on https://twitchapps.com/tmi/
BOT = "botftgTest"  # Bot's name [NO CAPITALS]
CHANNEL = "testftg"  # Channal name [NO CAPITALS]
OWNER = "testftg"  # Owner's name [NO CAPITALS]
### Functions

def writeFileLog(user,message):
    f = open("chat_log.txt", "a")
    f.write("Time="+str(datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S'))+" Name="+user+" Message="+message+"\n")
    
def writeFile(user,message):
    ### open chat.txt and append
    f = open("chat.txt", "a")
    ### write user name and message 
    f.write("Time="+str(datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S'))+" Name="+user+":"+message+"\n")

def sendToJava(user,message):
    (tcpCliSock, addr) = serverSocket.accept()
    tcpCliSock.sendall(str(str(datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S'))+","+user+","+message+",").encode('utf-8')+b"\n")
    tcpCliSock.close()
    
def sendMessage(s, message):
    messageTemp = "PRIVMSG #" + CHANNEL + " :" + message
    s.send((messageTemp + "\r\n").encode())
 
def getUser(line):
    separate = line.split(":", 2)
    user = separate[1].split("!", 1)[0]
    return user

def getMessage(line):
    global message
    try:
        message = (line.split(":", 2))[2]
    except:
        message = ""
    return message

def joinchat():
    readbuffer_join = "".encode()
    Loading = True
    while Loading:
        readbuffer_join = s.recv(1024)
        readbuffer_join = readbuffer_join.decode()
        temp = readbuffer_join.split("\n")
        readbuffer_join = readbuffer_join.encode()
        readbuffer_join = temp.pop()
        for line in temp:
            Loading = loadingCompleted(line)
    #os.remove("chat.txt")
    #os.remove("chat_log.txt")
    sendMessage(s, "Chat room joined!")
    print("Bot has joined " + CHANNEL + " Channel!")
 
def loadingCompleted(line):
    if ("End of /NAMES list" in line):
        return False
    else:
        return True
    
### Code start runs
s_prep = socket.socket()
## connect to java
serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
serverSocket.bind(("localhost", PORT_JAVA))
serverSocket.listen(5)
#s_prep.settimeout(20)
s_prep.connect((SERVER, PORT))
s_prep.send(("PASS " + PASS + "\r\n").encode())
s_prep.send(("NICK " + BOT + "\r\n").encode())
s_prep.send(("JOIN #" + CHANNEL + "\r\n").encode())
s = s_prep
joinchat()
readbuffer = ""
 
def Console(line):
    # gets if it is a user or twitch server
    if "PRIVMSG" in line:
        return False
    else:
        return True
 
while True:
        try:
            readbuffer = s.recv(1024)
            readbuffer = readbuffer.decode()
            temp = readbuffer.split("\n")
            readbuffer = readbuffer.encode()
            readbuffer = temp.pop()
        except:
            temp = ""
        for line in temp:
            if line == "":
                break
            # So twitch doesn't timeout the bot.
            if "PING" in line and Console(line):
                msgg = "PONG tmi.twitch.tv\r\n".encode()
                s.send(msgg)
                print(msgg)
                break
            # get user
            user = getUser(line)
            # get message send by user
            message = getMessage(line)
            # for you to see the chat from CMD             
            print(user + " > " + message)         
            # sends private msg to the user (start line)
            PMSG = "/w " + user + " "
 
################################# Command ##################################
############ Here you can add as meny commands as you wish of ! ############
############################################################################
            writeFileLog(user,message)#write log file
            if (message == "!p1\r") or (message == "!P1\r") or (message == "!1\r"):
                writeFile(user,"!1")
                sendToJava(user,"!1")
                break
            elif (message == "!p2\r") or (message == "!P2\r") or (message == "!2\r"):
                writeFile(user,"!2")
                sendToJava(user,"!2")
                break
            elif (message == "!zen\r") or (message == "!Zen\r") or (message == "!ZEN\r") or (message == "!Z\r") or (message == "!z\r"): 
                writeFile(user,"!ZEN")
                sendToJava(user,"!ZEN")
                break
            elif (message == "!garnet\r") or (message == "!Garnet\r") or (message == "!GARNET\r") or (message == "!G\r") or (message == "!g\r"): 
                writeFile(user,"!GARNET")
                sendToJava(user,"!GARNET")
                break
            elif (message == "!lud\r") or (message == "!Lud\r") or (message == "!LUD\r") or (message == "!L\r") or (message == "!l\r"): 
                writeFile(user,"!LUD")
                sendToJava(user,"!LUD")
                break
            #if (user == OWNER) and (message == "!next\r"):
            #    writeFile(user,"!next")
            #    sendMessage(s, "Next level")
            #    break
            #elif (user == OWNER) and (message == "!retry\r"):
            #    writeFile(user,"!retry")
            #    sendMessage(s, "Retry level")
            #    break  
            #elif (user == OWNER) and (message == "!s\r"):#Shoot
            #    writeFile(user,"!s")
            #    break 
            #elif (message == "!a\r")  or (message == "!A\r"):#Ability       
            #    writeFile(user,"!a")
            #    break            
            #elif (message == "!m\r")  or (message == "!M\r"):       
            #    writeFile(user,"!m")
            #    break
            #elif (message == "!l\r") or (message == "!L\r"):       
            #    writeFile(user,"!l")
            #    break
            #elif (message == "!u\r") or (message == "!U\r"):       
            #    writeFile(user,"!u")
            #    break
            #elif (message == "!d\r") or (message == "!D\r"):       
            #    writeFile(user,"!d")
            #    break
            #elif (user != OWNER) and (message == "!next\r"):
            #    sendMessage(s, "This is private command for ownner.")
            #    break   
            #elif (user != OWNER) and (message == "!retry\r"):
            #    sendMessage(s, "This is private command for ownner.")
            #    break               
            else:
                # Replace all non-alphanumeric non-# characters in a string.
                message = re.sub('[^0-9a-zA-Z#! ]+', '', message)
                if (message == ""):
                    break
                else :
                    writeFile(user,message)
                    break
                                
serverSocket.close()            
############################################################################
