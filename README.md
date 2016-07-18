# Telegram bot that finds books in flibusta fb2 archives
Since you are here you probably know that [flibusta](http://flibusta.is/) (big russian e-library) web ui [doesn't allow to download](http://flibusta.is/b/230948/fb2) a lot of books (due to copyright issues) as well as that all of them are available on Tor/I2P mirrors. One day I've got tired of that nonsense and written this bot, which gets book id / flibusta url and sends it to you in selected format (fb2, epub, mobi, pdf).

There is a [publicly available version](https://telegram.me/wubusta_bot) of this bot hosted on my home server. You can use it as long as it's not getting detrimental for me.

If you still want to run it by yourself, read on.

## Usage
There is a [full fb2 archive](http://flibusta.is/node/64756) of Flibusta distributed by torrent (about 180 GB on July 2016). You have to download it first and put on the server, where this app will be running.

Also you have to install Docker and Docker Compose on target machine because it is a preferable way to run everything up. Just get the [example compose configuration](https://gitlab.com/wutiarn/flibusta-bot/blob/master/docker-compose.example.yml), set telegram token, replace `/path/to/flibusta/archive` with actual path to flibusta library, rename config to `docker-compose.yml` and run `docker compose up -d`

**Note:** If you haven't heard anything about Docker or Docker Compose, please start with reading [Docker Overview](https://docs.docker.com/engine/understanding-docker/) and [Docker Compose Overview](https://docs.docker.com/compose/overview/)

## Environment variables description

| Variable            | Description                                                                         |
| ------------------- | ----------------------------------------------------------------------------------- |
| TELEGRAM_TOKEN      | Token, got from @BotFather bot (i.e. `258003162:AAFTKcGoZdgGrHJnArtCZtqaiQ8SiI6VPxw`) |

## Volume paths

| Volume              | Path                                                                                |
| ------------------- | ----------------------------------------------------------------------------------- |
| /code/data          | Folder with Flibusta archives that looks like `f.fb2-193389-195730.zip`             |