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

public class DownloadImage {
	
	public static void main( String[] args ) 
    {
    	
    	try {
			Mongo mongo = new Mongo("localhost", 27017);
			List<String> dbs = mongo.getDatabaseNames();
			
			DB db = mongo.getDB("ImageStore");
			DBCollection collection = db.getCollection("coll");
			
			String newFileName = "imagenr";

			// create a "photo" namespace
			GridFS gfsPhoto = new GridFS(db, "photo");

			// print the result
			DBCursor cursor = gfsPhoto.getFileList();
			while (cursor.hasNext()) {
				System.out.println(cursor.next());
			}
			// get image file by it's filename
			GridFSDBFile imageForOutput = gfsPhoto.findOne(newFileName);

			// save it into a new image file
			imageForOutput.writeTo("C:\\Users\\JoseEnrique\\Pictures\\"+imageForOutput.getFilename()+".png");
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
    	catch (Exception e) {
			e.printStackTrace();
		}

    }
}
