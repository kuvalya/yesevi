#!/bin/bash
PID=$1
NAME=$2
# 1 second repeat:
INTERVAL=1
MAX_CPU=0.0
MAX_MEM=0.0
while true;
do
    TOPOUT=$(top -b -n1 -p $PID | tail -n1 | awk '{print $9 " " $10}')
    read CPU MEM <<< $TOPOUT
    echo -ne "CPU=$CPU mem=$MEM \r"
    MAX_CPU=$(echo $CPU $MAX_CPU | awk '{if ($1 > $2) print $1; else print $2}')
    MAX_MEM=$(echo $MEM $MAX_MEM | awk '{if ($1 > $2) print $1; else print $2}')
    # -t for timeout, -N for just 1 character:
    read -t $INTERVAL -N 1 input
    if [[ $input = "q" ]] || [[ $input = "Q" ]]; then
        # prompt to appear on a new line.
echo
break
fi done
echo "$NAME REPORT:"
echo "Max CPU: $MAX_CPU%"
echo "Max Mem: $MAX_MEM%"