import axios from "axios";

export class productService  {
    
    static getProduct(){
      let dataUrl=`https://s3.amazonaws.com/roxiler.com/product_transaction.json`;
      return axios.get(dataUrl);
    }
  }
  