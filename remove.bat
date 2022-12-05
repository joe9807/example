echo off
for /L %%f in (1,1,%1) do call set "vv=%%vv%% joe%%f"

echo removing joe containers .....
docker rm --force %vv%

echo:
echo removing image .....
docker image rm example:0.0.1