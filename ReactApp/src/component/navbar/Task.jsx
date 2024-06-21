import React, { useEffect, useState } from 'react';
import '../../App.css';
import { Dropdown, Tooltip } from 'bootstrap';
import { Bar, BarChart, CartesianGrid, Label, Legend, ResponsiveContainer, XAxis, YAxis } from 'recharts';
import { productService } from '../service/ProductService';
import axios from 'axios';

const Navbar = () => {
  const [data, setData] = useState({

  })

  const [product, setProduct] = useState({
    product: [],
  });

  const soldCount = 0;
  const notSoldCount = 0;
  let amount;
  const [selectedMonth, setSelectedMonth] = useState('');
  const [currentPage, setCurrentPage] = useState(1);
  let [stat, setStat] = useState([])
  let [allPro, setAllPro] = useState({
    products: {}
  })



  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  const handleMonthChange = (e) => {
    setSelectedMonth(e.target.value);
    let promise = new Promise(async (res, rej) => {
      setStat({ ...stat })
      let responce = await axios.get(`http://localhost:8080/statistics${selectedMonth}`)
      console.log(responce.data.data[0]);
      amount = responce.data.data[0];
      console.log(amount);
      res(responce)
      promise.then((res1) => {
        setStat({ ...stat, stat: res1.data });
        console.log(stat.data);
      }).catch(() => {
        setStat({ ...stat, errorMessage: alert("data is not found") })
      })
    });

    let responce1 = axios.get(`http://localhost:8080/barchart?m=${selectedMonth}`).then((res2) => {
      setData({ ...data, data: res2.data })
      console.log(data);
    }).catch(() => {
      setData({ ...data, errorMessage: alert("data is not found") })
    })
    console.log(responce1);
  }
  const chartData = Object.keys(data).map(key => ({
    range: key,
    value: data[key]
  }));

  useEffect(() => {
    const fetchData = async () => {
      setAllPro({ ...allPro });
      try {
        const response = await axios.get(`http://localhost:8080/Allproducts`);
        console.log(response.data.data);
        setAllPro({ ...allPro, data: response.data.data }); // Update state with data
      } catch (error) {
        console.error('Error fetching data:', error);
        setAllPro({
          ...allPro,
          errorMessage: "Failed to load data. Please check your connection."
        });
      }
    };

    fetchData();
  }, []);
  let [query, setQuery] = useState({
    text: ""
  })
  let [searchPro, setSearchPro] = useState({
    data: []
  })

  const recordsPerPage = 10;
  const totalPages = Math.ceil(data.length / recordsPerPage);
  const startIndex = (currentPage - 1) * recordsPerPage;
  const endIndex = startIndex + recordsPerPage;
  const currentRecords = searchPro.data.slice(startIndex, endIndex);
  let searchContact = async (event) => {
    setQuery({ ...query, text: event.target.value })


    try {
      const response = await axios.get(`http://localhost:8080/products?text=${event.target.value}`);
      console.log(response.data.data);
      setSearchPro({ ...searchPro, data: response.data.data }); // Update state with data
    } catch (error) {
      console.error('Error fetching data:', error);
      setSearchPro({
        ...searchPro,
        errorMessage: "Failed to load data. Please check your connection."
      });
    }
    console.log(searchPro);
  }


  const getYearMonth = (dateStr) => {
    const date = new Date(dateStr);
    return (date.getMonth() + 1);
  };

  return (
    <>
      <div className='header text-center'>
        <h1 className='f-3'>Transaction dashboard</h1>
      </div>
      <div className="container-fluid">
        <div className="row justify-content-center">
          <div className="col-sm-2 text-center">
            <input type='text' onChange={searchContact} className='form-control bg-warning' placeholder='Search Transaction' />
          </div>
          <div className="col-sm text-center">
            <select className='btn bg-warning ms-5' value={selectedMonth} onChange={handleMonthChange}>
              <option value="">Select a month</option>
              <option className='dropdown-item' value="01">January</option>
              <option className='dropdown-item' value="02">February</option>
              <option className='dropdown-item' value="03">March</option>
              <option className='dropdown-item' value="04">April</option>
              <option className='dropdown-item' value="05">May</option>
              <option className='dropdown-item' value="06">June</option>
              <option className='dropdown-item' value="07">July</option>
              <option className='dropdown-item' value="08">August</option>
              <option className='dropdown-item' value="09">September</option>
              <option className='dropdown-item' value="10">October</option>
              <option className='dropdown-item' value="11">November</option>
              <option className='dropdown-item' value="12">December</option>
            </select>
          </div>
        </div>
        <div className="row justify-content-center">
          <table className="table table-hover border border-2 m-2">
            <thead>
              <tr>
                <th scope="col">Id</th>
                <th scope="col">Title</th>
                <th scope="col">Description</th>
                <th scope="col">Price</th>
                <th scope="col">Category</th>
                <th scope="col">Sold</th>
                <th scope="col">Image</th>
              </tr>
            </thead>
            <tbody>
              {searchPro.data && searchPro.data.map((item) => (
                <tr key={item.id}>
                  <td>{item.id}</td>
                  <td>{item.name}</td>
                  <td>{item.description}</td>
                  <td>{item.price}</td>
                  <td>{item.category}</td>
                  <td>{item.sold}</td>
                  <td><img src={item.description} alt="" style={{ width: "10px", height: "10px" }} /></td>
                </tr>
              ))}
            </tbody>
          </table>
          <nav className='navbar'>
            <div className="d-flex justify-content-center">
              <h6 className="me-3">Page No-{currentPage}</h6>
              <button
                onClick={() => handlePageChange(currentPage - 1)}
                disabled={currentPage === 1}
                className="btn btn-primary me-3"
              >
                Previous
              </button>
              <button
                onClick={() => handlePageChange(currentPage + 1)}
                disabled={currentPage === totalPages}
                className="btn btn-primary me-3"
              >
                Next
              </button>
              <h6>Per Page-10</h6>
            </div>
          </nav>
        </div>
        <div className="row justify-content-center">
          <div className="container">
            <div className="row">
              <div className="col-md-6 offset-md-3">
                <ul className="list-group m-5 text-center">
                  <h1>Statistics-{selectedMonth}</h1>
                  <li className="list-group-item bg-warning list-group-item-action">Total sale: <span className="fw-bold ms-1">{amount}</span></li>
                  <li className="list-group-item bg-warning list-group-item-action">Total Sold item: <span className="fw-bold ms-1">{soldCount}</span></li>
                  <li className="list-group-item bg-warning list-group-item-action">Total not sold item: <span className="fw-bold ms-1">{notSoldCount}</span></li>
                </ul>
              </div>
            </div>
          </div>
        </div>
        <div className="row justify-content-center">
          <ResponsiveContainer width="100%" height={400}>
            <BarChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="range" />
              <YAxis />
              {/* <Tooltip /> */}
              <Legend />
              <Bar dataKey="value" fill="#8884d8" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
    </>
  );
};

export default Navbar;
