import sys
from fabric import Connection

'''
使用Python3 + Fabric实现一键打包部署
参考：https://foofish.net/fabric.html

需要安装Python3环境和pip3
执行pip3 install fabric --upgrade 全局安装Fabric

打包部署管理端到dev环境，执行下面命令
pyhon3 deploy.py dev admin

打包部署应用端到dev环境，执行下面命令
pyhon3 deploy.py dev app 

'''
src_path = "../../"
app_src_path = "../../snapx-app"
bom_src_path = "../../snapx-bom"
core_src_path = "../../snapx-core"
servers = {
    "dev": {
        "host": "43.139.248.175",
        "user": "app",
        "key_file": "./keys/id_rsa_snapx_dev_app"
    }
}
apps={
    "admin": {
        "app_name": "snapx-admin",
        "target_path": "../../snapx-app/snapx-server-admin/target/snapx-server-admin-0.0.1-SNAPSHOT.jar",
        "deploy_path": "/app/srv/snapx/admin"
    },
    "app": {
        "app_name": "snapx-app",
        "target_path": "../../snapx-app/snapx-server-app/target/snapx-server-app-0.0.1-SNAPSHOT.jar",
        "deploy_path": "/app/srv/snapx/app"
    }
}

def main():
    server = sys.argv[1]
    env = servers.get(server)
    app_name = sys.argv[2]
    props = apps.get(app_name)
    with Connection(host=env["host"], user=env["user"], connect_kwargs={"key_filename": env["key_file"]}) as conn:
        pull(conn, props)
        package_bom(conn, props)
        package_core(conn, props)
        package_app(conn, props)
        deploy(conn, props)

def pull(conn, props):
    app_name=props["app_name"]
    print(f"开始拉取代码{app_name}")
    with conn.cd(src_path):
        conn.local("git pull")
    print(f"完成拉取代码{app_name}")

def package_bom(conn, props):
    app_name='snapx-bom'
    print(f"开始打包{app_name}")
    with conn.cd(bom_src_path):
        conn.local("mvn clean install")
    print(f"完成打包{app_name}")

def package_core(conn, props):
    app_name='snapx-core'
    print(f"开始打包{app_name}")
    with conn.cd(core_src_path):
        conn.local("mvn clean install -Dmaven.test.skip=true -U -T 2C")
    print(f"完成打包{app_name}")

def package_app(conn, props):
    app_name=props["app_name"]
    print(f"开始打包{app_name}")
    with conn.cd(app_src_path):
        conn.local("mvn clean install -Dmaven.test.skip=true -U -T 2C")
    print(f"完成打包{app_name}")

def deploy(conn, props):
    app_name=props["app_name"]
    target_path=props["target_path"]
    deploy_path= props["deploy_path"]

    print(f"开始上传{app_name}")
    conn.put(target_path, f"{deploy_path}/service.jar")
    print(f"完成上传{app_name}")

    print(f"开始重启{app_name}")
    with conn.cd(deploy_path):
        conn.run("script/stop.sh")
        conn.run("sleep 30")
        conn.run("script/start.sh")
    print(f"完成重启{app_name}")

if __name__ == '__main__':
    main()