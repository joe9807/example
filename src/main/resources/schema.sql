CREATE TABLE if not exists Example(
  id BIGSERIAL PRIMARY KEY,
  value VARCHAR(500),
  state varchar(100)
);

CREATE TABLE if not exists Transactions(
  id BIGSERIAL PRIMARY KEY,
  value VARCHAR(500)
);