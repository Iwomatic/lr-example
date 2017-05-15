#!/bin/bash
echo "Parando Jenkins..."
PID="$(pgrep -f jenkins)"
kill -9 ${PID}
exit