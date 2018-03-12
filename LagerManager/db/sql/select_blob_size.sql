-- in byte
SELECT *, OCTET_LENGTH(file) FROM anhang order by OCTET_LENGTH(file) desc;

-- in MB
SELECT *, OCTET_LENGTH(file)/1024/1024 FROM anhang order by OCTET_LENGTH(file) desc;