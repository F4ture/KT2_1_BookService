$ErrorActionPreference = "Stop"

if (-not (Get-Command bru -ErrorAction SilentlyContinue)) {
    throw "Bruno CLI не найден. Установите его: npm install -g @usebruno/cli"
}

$requiredUrls = @(
    "http://localhost:8761/eureka/apps",
    "http://localhost:8081/books",
    "http://localhost:8082/users",
    "http://localhost:8083/borrows"
)

foreach ($url in $requiredUrls) {
    try {
        Invoke-WebRequest -Uri $url -Method Get -TimeoutSec 5 -UseBasicParsing | Out-Null
    }
    catch {
        throw "Сервис недоступен: $url. Сначала запустите все четыре приложения."
    }
}

Push-Location $PSScriptRoot
try {
    bru run ".\02 Automated Flow" --env Local --bail
}
finally {
    Pop-Location
}
