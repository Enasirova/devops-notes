![](images/screenshot-20260304-141217.png)

from the slide: `cat /etc/nginx/conf.d/jenkins.conf`

```bash
[0 naseka@ad.ifortuna.cz@jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz ~]$ cat /etc/nginx/conf.d/jenkins.conf
    server {
        listen       443 ssl default_server;
        ssl_certificate "/etc/pki/jenkins/jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz.pem"; #this is our certificate file used by nginx
        ssl_certificate_key "/etc/pki/jenkins/jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz.key";
        ssl_session_cache shared:SSL:1m;
        ssl_session_timeout  10m;
        ssl_ciphers HIGH:!aNULL:!MD5;
        ssl_prefer_server_ciphers on;
        client_max_body_size 10M;
        proxy_connect_timeout       300;
        proxy_send_timeout          300;
        proxy_read_timeout          300;
        send_timeout                300;
        proxy_request_buffering off;
        server_name ci.svc.ifortuna.cz;
	access_log /var/log/nginx/jenkins.log combined;
	error_log /var/log/nginx/jenkins_error.log error;       
 
        location / {
          proxy_pass        http://localhost:8080;
          #proxy_redirect     off;
          proxy_redirect	http:// https://;
          proxy_set_header   Host $host;
          proxy_set_header   X-Real-IP $remote_addr;
          proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
          proxy_set_header   X-Forwarded-Host $server_name;
          proxy_set_header   X-Forwarded-Proto $scheme;
        }
    }

[0 naseka@ad.ifortuna.cz@jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz ~]$ 
```

verify validity dates with openssl:
`openssl x509 -in /etc/pki/jenkins/jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz.pem -noout -dates`

```bash
[0 naseka@ad.ifortuna.cz@jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz ~]$ openssl x509 -in /etc/pki/jenkins/jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz.pem -noout -dates
notBefore=Mar  4 15:35:00 2024 GMT
notAfter=Mar  5 15:35:00 2026 GMT
[0 naseka@ad.ifortuna.cz@jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz ~]$ 
```

maybe we have new certificates?


```bash
[1 naseka@ad.ifortuna.cz@jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz ~]$ cd /etc/pki/jenkins
[0 naseka@ad.ifortuna.cz@jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz jenkins]$ ls
jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz.key  master-jenkins-b-shared.m.dc1.cz.ipa.ifortuna.cz.key     master-jenkins-b-shared.m.dc1.cz.ipa.ifortuna.cz.pem
jenkins01-ocp01-shared.m.dc1.cz.ipa.ifortuna.cz.pem  master-jenkins-b-shared.m.dc1.cz.ipa.ifortuna.cz.keyOLD  master-jenkins-b-shared.m.dc1.cz.ipa.ifortuna.cz.pemOLD
```

i cannot see any new ones.. tell roman