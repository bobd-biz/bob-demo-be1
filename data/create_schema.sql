create schema if not exists `bob-demo-1`;
use `bob-demo-1`;

create user if not exists 'bob-demo-1'@'localhost' identified by 'bob-demo-1';
grant CREATE, ALTER, DROP, INSERT, UPDATE, DELETE, SELECT, REFERENCES on `bob-demo-1`.* TO 'bob-demo-1'@'localhost';
flush PRIVILEGES;
