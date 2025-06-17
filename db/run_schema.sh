#!/bin/bash

# Config
DEFAULT_HOST="localhost"
DEFAULT_PORT="5432"
SQL_FILE="db/create_chat_schema.sql"
DB_NAME="communication"
DB_USER="core_api_user"

HOST=${1:-$DEFAULT_HOST}
PORT=${2:-$DEFAULT_PORT}

# Create DB if not exists
if ! psql -h "$HOST" -p "$PORT" -U "$DB_USER" -lqt | cut -d \| -f 1 | grep -qw "$DB_NAME"; then
    echo "Creating database '$DB_NAME'..."
    if createdb -h "$HOST" -p "$PORT" -U "$DB_USER" -E UTF8 -O "$DB_USER" "$DB_NAME"; then
        echo "✅ Database '$DB_NAME' created"
    else
        echo "❌ Failed to create database '$DB_NAME'"
        exit 1
    fi
else
    echo "ℹ️ Database '$DB_NAME' already exists"
fi

# Apply schema
echo "🏗️  Applying schema from $SQL_FILE..."
if psql -h "$HOST" -p "$PORT" -U "$DB_USER" -d "$DB_NAME" -f "$SQL_FILE"; then
    echo "✅ Schema applied successfully"
else
    echo "❌ Failed to apply schema"
    exit 1
fi
