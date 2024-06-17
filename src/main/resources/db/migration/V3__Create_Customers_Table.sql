CREATE TABLE IF NOT EXISTS customers (
  id SERIAL NOT NULL PRIMARY KEY,
  firstname TEXT NOT NULL,
  lastname TEXT NOT NULL,
  companyname TEXT NOT NULL
);
