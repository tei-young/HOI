#!/bin/bash

echo "================================"
echo "  HoiMC Plugin Build Script"
echo "================================"
echo ""

# Maven이 설치되어 있는지 확인
if ! command -v mvn &> /dev/null
then
    echo "❌ Maven이 설치되어 있지 않습니다."
    echo "Maven을 먼저 설치해주세요: https://maven.apache.org/download.cgi"
    exit 1
fi

echo "📦 Maven을 사용하여 프로젝트를 빌드합니다..."
echo ""

# 빌드 실행
mvn clean package

# 빌드 결과 확인
if [ $? -eq 0 ]; then
    echo ""
    echo "================================"
    echo "  ✅ 빌드 성공!"
    echo "================================"
    echo ""
    echo "생성된 파일:"
    ls -lh target/*.jar | grep -v "original"
    echo ""
    echo "JAR 파일 위치: target/hoimc-plugin-1.0.0-SNAPSHOT.jar"
    echo ""
    echo "서버에 설치하려면:"
    echo "cp target/hoimc-plugin-1.0.0-SNAPSHOT.jar <서버폴더>/plugins/"
else
    echo ""
    echo "================================"
    echo "  ❌ 빌드 실패"
    echo "================================"
    echo ""
    echo "위의 오류 메시지를 확인하세요."
    exit 1
fi
