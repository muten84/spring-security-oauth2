INSERT
INTO WEB_SESSIONS
  (
    id,
    WEB_IDENTITY_ID,
    LOGIN_TIMESTAMP,
    ADDRESS,
    SESSION_ID
  )
  VALUES
  (
    'TEST',
    'TEST',
    NOW (),
    '127.0.0.1',
    'TOKEN'
  );