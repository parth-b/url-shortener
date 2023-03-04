
CREATE TABLE  urls (
  originalURL                    VARCHAR(60)  DEFAULT RANDOM_UUID() PRIMARY KEY ,
  hashURL                        VARCHAR      NOT NULL UNIQUE,
  hits                           INTEGER
);