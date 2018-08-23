'use strict'

const
    electron = require('electron'),
    path = require('path'),
    config = require('../config/electron'),
    app = electron.app,
    BrowserWindow = electron.BrowserWindow




let mainWindow

function createWindow() {
    /**
     * Initial window options
     */
    mainWindow = new BrowserWindow({
        title: config.name,
        width: config.device.panasonicFZG1.width,
        height: config.device.panasonicFZG1.height,
        icon: path.join(__dirname, '../icons/icon.png')
    })

    console.log("running: " + process.env.NODE_ENV);
    //|| require('../../config').dev.port}
    mainWindow.loadURL(
        process.env.NODE_ENV === 'production' ?
        `file://${__dirname}/index.html` :
        `http://localhost:4200`
    )

    if (process.env.NODE_ENV === 'development') {
        BrowserWindow.addDevToolsExtension(path.join(__dirname, '../node_modules/devtron'))

        let installExtension = require('electron-devtools-installer')

        installExtension.default(installExtension.ANGULARJS_BATARANG)
            .then(name => mainWindow.webContents.openDevTools())
            .catch(err => console.log('An error occurred: ', err))
    }

    mainWindow.on('closed', () => {
        mainWindow = null
    })
}

app.on('ready', createWindow)

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        app.quit()
    }
})

app.on('activate', () => {
    if (mainWindow === null) {
        createWindow()
    }
})
