# spring-boot-image-api

#### Technologies
- Java 17
- MySQL
- JPA
- Spring Boot
- GCP Storage
- GCP Cloud SQL

### Architecture

For the Image Processing API, I decided to use Google Vision, signing up for the free 90 day trial and credits to use Google services. Because of this, I opted to also use Google Cloud SQL for persistence and Google Cloud Storage for data storage.
There are two approaches to storing images. Either I could store the bytes of an image in a LONGBLOB in the db or I could storage the data in a file/object based storage solution. I opted to use Google Cloud Storage, as I feel this approach is more scalable (bloating up the db with BLOBs could get nasty). 

Storing images in Cloud Storage

PROS
- scalable
- separate image data from metadata
- supports very large images

CONS
- logic and migrations increase in complexity

### Design

I have a single Controller that handles the API for the images. 
For the domain model, I have two models: ImageData and ImageTag. 

ImageData represents an uploaded image and holds most of the metadata. 

ImageTags represent a single object that was tagged or detected by the vision api. 

I leverage the ImageTag model for querying for Images with a particular tag. 

