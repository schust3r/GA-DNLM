package com.mongoCon.mongoCon;

import java.io.File;
import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

public class UploadImage {
	public static void main( String[] args ) 
    {
    	
    	try {
    		
			Mongo mongo = new Mongo("localhost", 27017);
			List<String> dbs = mongo.getDatabaseNames();
			
			DB db = mongo.getDB("ImageStore");
			DBCollection collection = db.getCollection("coll");
			
			String newFileName = "imagenr";

			File imageFile = new File("C:\\Users\\JoseEnrique\\Desktop\\imagenr.jpg");

			// create a "photo" namespace
			GridFS gfsPhoto = new GridFS(db, "photo");

			// get image file from local drive
			GridFSInputFile gfsFile = gfsPhoto.createFile(imageFile);

			// set a new filename for identify purpose
			gfsFile.setFilename(newFileName);

			// save the image file into mongoDB
			gfsFile.save();
			
			System.out.println(gfsFile);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    	catch (Exception e) {
			e.printStackTrace();
		}

    }
}
