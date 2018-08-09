// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
  production: false,
  configuration: {
    global: {
      listItemHeight: "1:1",
      listItemCols: "5",
      confirmDialogWidth: '900px',
      confirmAllDataPatient: false
    },
    service: {
      host: '127.0.0.1',
      port: '8989'
    },
    security: {
      credentials: {
        username: 'emsmobile',
        password: '3m5m0b1l3',
      }
    },
    logging: {
      type: 1
    }
  },
  cdn: {
    url: "http://localhost:4200"
  }
};
