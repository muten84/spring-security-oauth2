//const { spawn } = require('child_process');
//const child = spawn('npm', ['run', 'serve']);
//const electron = spawn('npm', ['run', 'dev']);

/* child.stdout.on('data', (data) => {
    console.log(`child stdout:\n${data}`);
});

child.on('exit', function(code, signal) {
    console.log('child process exited with ' +
        `code ${code} and signal ${signal}`);
}); */

const { exec } = require('child_process');
console.log("serving angular app....");
exec('npm run serve', (err, stdout, stderr) => {
    console.log("running....");

    if (err) {
        console.error(`exec error: ${err}`);
        return;
    }

    console.log(`${stdout}`);
});

setTimeout(() => {
    console.log("running electron app wrapper....");

    exec('npm run dev', (err, stdout, stderr) => {
        console.log("running....");

        if (err) {
            console.error(`exec error: ${err}`);
            return;
        }

        console.log(`${stdout}`);
    });
}, 5000);