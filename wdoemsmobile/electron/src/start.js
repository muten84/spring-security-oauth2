const electron = require('electron');
const loadJsonFile = require('load-json-file');
const http = require('http');
/*const net = require('net');
 */

import {
    app,
    BrowserWindow,
    Menu
} from 'electron';
app.commandLine.appendSwitch('disable-web-security'); // try add this line

// Keep a global reference of the window object, if you don't, the window will
// be closed automatically when the JavaScript object is garbage collected.
let mainWindow;
let devMode = true;
/*var screenElectron = app.screen;
 */

function loadElectronConfig(callback) {

    return http.get({
        host: '127.0.0.1',
        port: "8989",
        path: '/web/electron.json'
    }, function (response) {
        // Continuously update stream with data
        var body = '';
        var err = undefined;
        response.on('data', function (d) {
            body += d;
        });

        response.on('end', function () {

            // Data reception is done, do whatever with it!
            try {
                console.log("trying to parse body: " + body)
                var parsed = JSON.parse(body);
            } catch (err) {
                console.log(err);
                callback({
                    data: {}
                });
                return;
            }
            callback({
                data: parsed
            });

        });
    });
}

function loadInputFromFile(callback) {

    return http.get({
        host: '127.0.0.1',
        port: "8989",
        path: '/web/input.json'
    }, function (response) {
        // Continuously update stream with data
        var body = '';
        var err = undefined;
        response.on('data', function (d) {
            body += d;
        });

        response.on('end', function () {

            // Data reception is done, do whatever with it!
            try {
                var parsed = JSON.parse(body);
            } catch (err) {
                callback({
                    data: {}
                });
                return;
            }
            callback({
                data: parsed
            });

        });
    });

}


const createWindow = () => {
    var monitorWidth = 800;
    var monitorHeight = 600;

    // Create the browser window.

    const {
        width,
        height
    } = electron.screen.getPrimaryDisplay().workAreaSize

    loadElectronConfig(function (config) {
        console.log("config electron is: " + JSON.stringify(config));
        if (config.data.devMode) {
            devMode = config.data.devMode
        } else {
            devMode = false;
        }
        mainWindow = new BrowserWindow(config.data);

        loadInputFromFile(function (response) {
            //"url": "http://localhost:4200/ricerca/", 
            if (!response.data || !response.data.url) {
                mainWindow.close();
                return;
            }
            console.log("loading url is: " + response.data.url);
            mainWindow.loadURL(response.data.url);
            let ses = mainWindow.webContents.session;
            console.log(ses);
            /*ses.fromPartition('', {
              cache: false
            });*/
            // Open the DevTools.
            if (devMode) {
                mainWindow.webContents.openDevTools();
            }
            if (!devMode) {
                Menu.setApplicationMenu(null);
            }

            mainWindow.onbeforeunload = (e) => {
                console.log('I do not want to be closed');
                // e.returnValue = false; // equivalent to `return false` but not recommended
                mainWindow.webContents.executeJavaScript("window.sessionStorage.removeItem('application_status');");
                mainWindow.webContents.executeJavaScript("window.sessionStorage.removeItem('device_status');");
            }

            mainWindow.on('session-end', () => {
                console.log("on force shutdown");
                mainWindow.webContents.executeJavaScript("window.sessionStorage.removeItem('application_status');");
                mainWindow.webContents.executeJavaScript("window.sessionStorage.removeItem('device_status');");
                // mainWindow = null;
            });


            mainWindow.on('closed', () => {
                console.log("on window closed");

                mainWindow = null;
            });

            mainWindow.webContents.on('did-finish-load', function () {
                console.log("did finish load");
                loadInputFromFile(function (response) {
                    if (response) {
                        console.log("input received: " + JSON.stringify(response.data.credentials));
                        console.log("input received: " + response.data.filter);
                        mainWindow.webContents.executeJavaScript("window.sessionStorage.removeItem('application_status');");
                        mainWindow.webContents.executeJavaScript("window.sessionStorage.removeItem('device_status');");
                        if (!response.data) {
                            return;
                        }
                        if (response.data.filter) {
                            mainWindow.webContents.executeJavaScript("window.sessionStorage.removeItem('application_status');");
                            mainWindow.webContents.executeJavaScript("window.sessionStorage.removeItem('device_status');");
                        }
                        if (response.data.credentials) {
                            mainWindow.webContents.executeJavaScript("window.sessionStorage.removeItem('application_status');");
                            mainWindow.webContents.executeJavaScript("window.sessionStorage.removeItem('device_status');");
                        }
                    }
                });


            });
        });
    });
};

// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.

app.on('ready', createWindow);

// Quit when all windows are closed.
app.on('window-all-closed', () => {
    // On OS X it is common for applications and their menu bar
    // to stay active until the user quits explicitly with Cmd + Q
    if (process.platform !== 'darwin') {
        app.quit();
    }
});

app.on('activate', () => {
    // On OS X it's common to re-create a window in the app when the
    // dock icon is clicked and there are no other windows open.
    if (mainWindow === null) {
        createWindow();
    }
});

// In this file you can include the rest of your app's specific main process
// code. You can also put them in separate files and import them here.
