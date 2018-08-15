// Dependecies

const child_process = require('child_process');
const I2C = require('i2c-bus');
const dgram = require('dgram');

// Public variables

const HOST = '192.168.1.1';
const udpPORT = 8888;
const streamPORT = 8889;
const I2Caddress = 0x12;

// Functions/objects

const arduino = I2C.openSync(1);

const udpserver = dgram.createSocket('udp4');
udpserver.on('error', function (error) {
    console.log('Error: ' + error);
    udpserver.close();
});
udpserver.on('listening', function () {
    var address = udpserver.address();
    var port = address.port;
    var family = address.family;
    var ipaddr = address.address;
    console.log('UDP listening at port' + port);
    console.log('UDP ip :' + ipaddr);
    console.log('UDP is IPvX : ' + family);
});
udpserver.on('message', function (data, remote) {
    console.log(remote.address + ':' + remote.port + ' - ' + data);
    if (data == "start") {
        console.log("Démarre la cammera");
        startCam();
    } else if (data == "stop") {
        console.log("Arette la camera");
        stopCam();
    } else {
        arduino.i2cWrite(I2Caddress, 2, new Buffer.from([Number(data.subarray(0, data.indexOf(" "))), Number(data.subarray(data.indexOf(" ") + 1, data.length))]), function (err, writen, buffer) {
            if (err && data == "180 180") {
                arduino.i2cWrite(I2Caddress, 2, new Buffer.from([Number(data.subarray(0, data.indexOf(" "))), Number(data.subarray(data.indexOf(" ") + 1, data.length))]), function (err2, writen, buffer) {
                    if (err2) {
                        console.log(err);
                        console.log(err2);
                    }
                });
            } else if (writen != 2) {
                console.log("byte manquant");
            }
        });
    }
});

const startCam = function () {
    arduino.i2cWrite(I2Caddress, 1, new Buffer.from([222]), function (err, bytes, buffer) {
        if (err) {
            console.log(err);
        }
    });
};
const stopCam = function () {
    arduino.i2cWrite(I2Caddress, 1, new Buffer.from([111]), function (err, bytes, buffer) {
        if (err) {
            console.log(err);
        }
    });
};

// Start

console.log(child_process.execSync('sudo uv4l -nopreview --auto-video_nr --driver raspicam --encoding mjpeg --width 640 --height 480 --server-option \'--port=' + streamPORT + '\''));

udpserver.bind(udpPORT, HOST);
console.log("UDP server up");
