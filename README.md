# Rogue's Gallery

This is a training project for a fictional web service that allows users
to story NPCs for a table top role playing game.


## Docker Files

The database lives in docker/database

To create the base image:

    docker build -t roguedb:v1 roguedb:latest

To run the database:

    docker run -d -p 5432:5432 --name rogue-postgres roguedb:v1

The database can be accessed via any PostgreSQL client on localhost at
port 5432 (the standard port).  Username is *blakeney* and password is
*scarletpimpernel* with a database named *blakeney*  These values can be
changed by modifying the appropriate environment variables in the
dockerfile and rebuilding the image and container. 
