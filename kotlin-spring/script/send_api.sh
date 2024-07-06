#!/bin/bash

URL="http://localhost:9797/study-kotlin/v1/resource2"  # 호출할 API 엔드포인트
TOTAL_REQUESTS=100  # 보낼 총 요청 수

for ((i=1; i<=TOTAL_REQUESTS; i++))
do
  curl -X GET "$URL" &
  sleep 0.1  # 100밀리초 대기
done