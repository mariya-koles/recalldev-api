-- Database initialization script for PostgreSQL
-- This script creates the necessary tables and types for the recall.dev application

-- Create the difficulty_level enum type
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'difficulty_level') THEN
        CREATE TYPE difficulty_level AS ENUM ('EASY', 'MEDIUM', 'HARD');
    END IF;
END$$;

-- Create the questions table
CREATE TABLE IF NOT EXISTS questions (
    id BIGSERIAL PRIMARY KEY,
    question_text TEXT NOT NULL,
    question_answer TEXT NOT NULL,
    difficulty difficulty_level NOT NULL DEFAULT 'MEDIUM'
);

-- Create the tags table
CREATE TABLE IF NOT EXISTS tags (
    id BIGSERIAL PRIMARY KEY,
    name TEXT UNIQUE NOT NULL
);

-- Create the question_tags junction table
CREATE TABLE IF NOT EXISTS question_tags (
    question_id BIGINT REFERENCES questions(id) ON DELETE CASCADE,
    tag_id BIGINT REFERENCES tags(id) ON DELETE CASCADE,
    PRIMARY KEY (question_id, tag_id)
);

-- Insert some sample data (optional)
INSERT INTO tags (name) VALUES 
    ('algorithms'),
    ('data-structures'),
    ('system-design'),
    ('java'),
    ('spring-boot')
ON CONFLICT (name) DO NOTHING;

INSERT INTO questions (question_text, question_answer, difficulty) VALUES 
    ('What is the time complexity of binary search?', 'O(log n) - Binary search eliminates half of the search space in each iteration.', 'MEDIUM'),
    ('Explain the difference between ArrayList and LinkedList in Java.', 'ArrayList uses dynamic arrays for storage with O(1) random access but O(n) insertion/deletion. LinkedList uses doubly-linked nodes with O(1) insertion/deletion but O(n) random access.', 'EASY'),
    ('How would you design a URL shortener like bit.ly?', 'Key components: URL encoding/decoding service, database for mappings, caching layer, load balancer, analytics service. Use base62 encoding for short URLs and consider sharding for scale.', 'HARD')
ON CONFLICT DO NOTHING; 