var devMode = true; //switch this to enable production

var locale = "it-it";
var contextApp = "sdoto-docservice-war";
var restBasePath = "rest";
var extPort = 8082;
var extHost = '127.0.0.1';
var expiresInDays = 1;

if (!devMode) {
  locale = "it-it";
  contextApp = "sdoto-docservice-war";
  restBasePath = "rest";
  devMode = false;
  extPort = 80;
  expiresInDays = 1
}
