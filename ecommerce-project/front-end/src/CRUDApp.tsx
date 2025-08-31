import { useEffect, useState } from "react";
import axios from "axios";

const API_URL = "/product/api/v1/products";
// const API_URL = "product.default.svc.cluster.local:80/product/api/v1/products";
// const API_URL = "http://localhost:8081/product/api/v1/products";


// Types
interface Product {
    id?: number;
    name: string;
    price: number
    quantity: number;
}


const CrudApp = () => {
    // 🔧 Bien typer les states
    const [products, setProducts] = useState<Product[]>([]);
    const [newProduct, setNewProduct] = useState<Product>({ name: "", price: 0, quantity: 0 });
    const [editingProduct, setEditingProduct] = useState<Product | null>(null);

    // Read
    const fetchProducts = async () => {
        try {
            const response = await axios.get<Product[]>(API_URL);
            setProducts(response.data);
        } catch (error) {
            console.error("Error fetching users:", error);
        }
    };

    useEffect(() => {
        fetchProducts();
    }, []);

    // Create
    const addUser = async () => {
        if (!newProduct.name || !newProduct.price || !newProduct.quantity) {
            alert("All fields are required");
            return;
        }
        try {
            const response = await axios.post<Product>(API_URL, newProduct);

            //const token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJka0o1bXdNT1N3N1QtaVc3Y0Q3eFZkSlNKWmtrLVRiN0lqNm9fZDNJN3VvIn0.eyJleHAiOjE3NTY2NzA3NzUsImlhdCI6MTc1NjY3MDQ3NSwiYXV0aF90aW1lIjoxNzU2NjY3NDAyLCJqdGkiOiI0NTVhZjhiZi1iZmJhLTRiMjctOWVmOS01NzFiNGNiMWMyODIiLCJpc3MiOiJodHRwczovL2tleWNsb2FrLmFkam9kYS5jb20ubmdyb2suYXBwL3JlYWxtcy9hZGpvZGEtZGV2LXJlYWxtIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjgwNGYwZjgwLTY3NzQtNDhlOS04MzI3LTc1MmIwYTk3MTM0ZCIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkam9kYS13ZWJhcHAiLCJzZXNzaW9uX3N0YXRlIjoiNDM5MWRiODAtOTA0NS00ZjU0LTg3OTMtYzU1NmI4MGViOTZkIiwiYWNyIjoiMCIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIiwiaHR0cDovL2Fkam9kYWRldi5jb20iXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkNIRUZfQUdFTkNFIiwib2ZmbGluZV9hY2Nlc3MiLCJkZWZhdWx0LXJvbGVzLWFkam9kYS1kZXYtcmVhbG0iLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIGVtYWlsIHByb2ZpbGUiLCJzaWQiOiI0MzkxZGI4MC05MDQ1LTRmNTQtODc5My1jNTU2YjgwZWI5NmQiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IktvZmZpIE1hd3VsaSBBREpPREEiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJtYXd1bGkiLCJnaXZlbl9uYW1lIjoiS29mZmkgTWF3dWxpIiwiZmFtaWx5X25hbWUiOiJBREpPREEiLCJlbWFpbCI6ImtvZmZpbWF3dWxpLmFkam9kYUBnbWFpbC5jb20ifQ.k2X5mdq7hPgowDN-FPBn919CDKfDHb36xWdK8GsvAbnNnBTpr0Es6qb8bYpQivDuRH82xHdFuCDYA2h4BNAtp5tQLDNLNdREpAzh42dD53CiSeshIelHMll94LFiFU9DVVYVa3Vv9kg4IwDhFeI7Bu3E16ERxklI1b6Dbwk1SGU_F00xuM55e6w952h8_ClQX54v4e0dhQAFzI-Dq05y74gADEf77qRtlMzcBONhPlqimPkA78GdMHDeSivsXvXz_-9cMZg3BCrlfkLGYCEAfEAp44CuG_cxGozEswAttEEViCVlg4YJ2YIdPCeuK1Wzpf9wfkBYgHzNdLv03vayFA";
            //const response = await axios.post<Product>(
            //    API_URL,
            //    newProduct,
            //    {
            //        headers: {
            //            Authorization: `Bearer ${token}`,
            //            "Content-Type": "application/json",
            //        },
            //    }
            //);

            setProducts((prev) => [...prev, response.data]);
            setNewProduct({ name: "", price: 0, quantity: 0 });
        } catch (error) {
            console.error("Error adding Product:", error);
        }
    };

    // Update
    const updateProduct = async () => {
        if (!editingProduct?.id) return;
        try {


            await axios.put<Product>(`${API_URL}/${editingProduct.id}`, editingProduct);

            //const token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJka0o1bXdNT1N3N1QtaVc3Y0Q3eFZkSlNKWmtrLVRiN0lqNm9fZDNJN3VvIn0.eyJleHAiOjE3NTY2NzE5MzMsImlhdCI6MTc1NjY3MTYzMywiYXV0aF90aW1lIjoxNzU2NjY3NDAyLCJqdGkiOiI3YmM0NTgwZC00MWUxLTQzMWEtYjk3NC1kZDUzZGQzOGI1MjgiLCJpc3MiOiJodHRwczovL2tleWNsb2FrLmFkam9kYS5jb20ubmdyb2suYXBwL3JlYWxtcy9hZGpvZGEtZGV2LXJlYWxtIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjgwNGYwZjgwLTY3NzQtNDhlOS04MzI3LTc1MmIwYTk3MTM0ZCIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkam9kYS13ZWJhcHAiLCJzZXNzaW9uX3N0YXRlIjoiNDM5MWRiODAtOTA0NS00ZjU0LTg3OTMtYzU1NmI4MGViOTZkIiwiYWNyIjoiMCIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIiwiaHR0cDovL2Fkam9kYWRldi5jb20iXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkNIRUZfQUdFTkNFIiwib2ZmbGluZV9hY2Nlc3MiLCJkZWZhdWx0LXJvbGVzLWFkam9kYS1kZXYtcmVhbG0iLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIGVtYWlsIHByb2ZpbGUiLCJzaWQiOiI0MzkxZGI4MC05MDQ1LTRmNTQtODc5My1jNTU2YjgwZWI5NmQiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IktvZmZpIE1hd3VsaSBBREpPREEiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJtYXd1bGkiLCJnaXZlbl9uYW1lIjoiS29mZmkgTWF3dWxpIiwiZmFtaWx5X25hbWUiOiJBREpPREEiLCJlbWFpbCI6ImtvZmZpbWF3dWxpLmFkam9kYUBnbWFpbC5jb20ifQ.VNtAmKeqYPttYUfTviVj0glbCIBvt21Xy8kYvX9zI62nnoRVioqRVPL6XTr0qIUo9srnJ3UJaAZUGnsNbJBf6_8wYXNXxe22dxavBHFoxSVQf8WtuOqkue3VATNEaOzDiDabI6KYDorWaRHKODSYbnlDk8K3xP8OAKWAPNZs3yAPwJuw1howu5H5Ohld9Fy6GrnaLJBa-HAl-7TxnSOg2euR1zND5oGf1jAYCQi9mncxCMF6ESHtYJQqs7JijEzXcIj-35_fC5Nt0dO2sAo-xwDL_w_0oaJKzTlESD655ttRMJmJ1IIyaZCEMAlkWIW6TqR-Kreh28dPd5bGoXtEug";
            //await axios.put<Product>(`${API_URL}/${editingProduct.id}`, editingProduct,
            //    {
            //        headers: {
            //            Authorization: `Bearer ${token}`,
            //            "Content-Type": "application/json",
            //        },
            //    });
            setProducts((prev) =>
                prev.map((u) => (u.id === editingProduct.id ? editingProduct : u))
            );
            setEditingProduct(null);
        } catch (error) {
            console.error("Error updating product:", error);
        }
    };

    // Delete
    const deleteProduct = async (id: number) => {
        try {
           await axios.delete(`${API_URL}/${id}`);

            // const token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJka0o1bXdNT1N3N1QtaVc3Y0Q3eFZkSlNKWmtrLVRiN0lqNm9fZDNJN3VvIn0.eyJleHAiOjE3NTY2NzA3NzUsImlhdCI6MTc1NjY3MDQ3NSwiYXV0aF90aW1lIjoxNzU2NjY3NDAyLCJqdGkiOiI0NTVhZjhiZi1iZmJhLTRiMjctOWVmOS01NzFiNGNiMWMyODIiLCJpc3MiOiJodHRwczovL2tleWNsb2FrLmFkam9kYS5jb20ubmdyb2suYXBwL3JlYWxtcy9hZGpvZGEtZGV2LXJlYWxtIiwiYXVkIjoiYWNjb3VudCIsInN1YiI6IjgwNGYwZjgwLTY3NzQtNDhlOS04MzI3LTc1MmIwYTk3MTM0ZCIsInR5cCI6IkJlYXJlciIsImF6cCI6ImFkam9kYS13ZWJhcHAiLCJzZXNzaW9uX3N0YXRlIjoiNDM5MWRiODAtOTA0NS00ZjU0LTg3OTMtYzU1NmI4MGViOTZkIiwiYWNyIjoiMCIsImFsbG93ZWQtb3JpZ2lucyI6WyIqIiwiaHR0cDovL2Fkam9kYWRldi5jb20iXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIkNIRUZfQUdFTkNFIiwib2ZmbGluZV9hY2Nlc3MiLCJkZWZhdWx0LXJvbGVzLWFkam9kYS1kZXYtcmVhbG0iLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIGVtYWlsIHByb2ZpbGUiLCJzaWQiOiI0MzkxZGI4MC05MDQ1LTRmNTQtODc5My1jNTU2YjgwZWI5NmQiLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwibmFtZSI6IktvZmZpIE1hd3VsaSBBREpPREEiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJtYXd1bGkiLCJnaXZlbl9uYW1lIjoiS29mZmkgTWF3dWxpIiwiZmFtaWx5X25hbWUiOiJBREpPREEiLCJlbWFpbCI6ImtvZmZpbWF3dWxpLmFkam9kYUBnbWFpbC5jb20ifQ.k2X5mdq7hPgowDN-FPBn919CDKfDHb36xWdK8GsvAbnNnBTpr0Es6qb8bYpQivDuRH82xHdFuCDYA2h4BNAtp5tQLDNLNdREpAzh42dD53CiSeshIelHMll94LFiFU9DVVYVa3Vv9kg4IwDhFeI7Bu3E16ERxklI1b6Dbwk1SGU_F00xuM55e6w952h8_ClQX54v4e0dhQAFzI-Dq05y74gADEf77qRtlMzcBONhPlqimPkA78GdMHDeSivsXvXz_-9cMZg3BCrlfkLGYCEAfEAp44CuG_cxGozEswAttEEViCVlg4YJ2YIdPCeuK1Wzpf9wfkBYgHzNdLv03vayFA";
            // await axios.delete(`${API_URL}/${id}`,
            //                 {
            //                     headers: {
            //                         Authorization: `Bearer ${token}`,
            //                         "Content-Type": "application/json",
            //                     },
            //                 });

            setProducts((prev) => prev.filter((u) => u.id !== id));
        } catch (error) {
            console.error("Error deleting product:", error);
        }
    };

    return (
        <div style={{marginLeft: "5%", marginRight: "5%"}}>

            <p className="text-center">ADJODA-DEV CRUD APP</p>

            <div style={{margin: "2%"}} />

            {/* Add User */}
            <div>
                <input
                    type="text"
                    className="bg-transparent placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded-md px-3 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm focus:shadow"
                    placeholder="Name"
                    value={newProduct.name}
                    onChange={(e) => setNewProduct({...newProduct, name: e.target.value})}
                />
                <input
                    type="number"
                    className="bg-transparent placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded-md px-3 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm focus:shadow"
                    placeholder="Price"
                    value={newProduct.price}
                    onChange={(e) => setNewProduct({...newProduct, price: Number(e.target.value)})}
                />

                <input
                    type="number"
                    className="bg-transparent placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded-md px-3 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm focus:shadow"
                    placeholder="Quantity"
                    value={newProduct.quantity}
                    onChange={(e) => setNewProduct({...newProduct, quantity: Number(e.target.value)})}
                />
                <button type={"button"}
                        className="rounded-md border border-slate-300 py-2 px-4 text-center text-sm transition-all shadow-sm hover:shadow-lg text-slate-600 hover:text-white hover:bg-slate-800 hover:border-slate-800 focus:text-white focus:bg-slate-800 focus:border-slate-800 active:border-slate-800 active:text-white active:bg-slate-800 disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none"
                        onClick={addUser}>
                    Add Product
                </button>
            </div>

            <div style={{margin: "2%"}} />

            {/* Search */}
            <div className="w-full ">
                <div className="relative">
                    <input
                        className="w-full bg-transparent placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded-md pl-3 pr-28 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm focus:shadow"
                        placeholder="Search ..."
                    />
                    <button
                        className="absolute top-1 right-1 flex items-center rounded bg-slate-800 py-1 px-2.5 border border-transparent text-center text-sm text-white transition-all shadow-sm hover:shadow focus:bg-slate-700 focus:shadow-none active:bg-slate-700 hover:bg-slate-700 active:shadow-none disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none"
                        type="button"
                    >
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="currentColor"
                             className="w-4 h-4 mr-2">
                            <path fillRule="evenodd"
                                  d="M10.5 3.75a6.75 6.75 0 1 0 0 13.5 6.75 6.75 0 0 0 0-13.5ZM2.25 10.5a8.25 8.25 0 1 1 14.59 5.28l4.69 4.69a.75.75 0 1 1-1.06 1.06l-4.69-4.69A8.25 8.25 0 0 1 2.25 10.5Z"
                                  clipRule="evenodd"/>
                        </svg>

                        Search
                    </button>
                </div>
            </div>
            <div style={{margin: "2%"}} />

            {/* Edit User */}
            {editingProduct && (
                <div style={{margin: "2%"}}>
                    <input
                        type="text"
                        className="bg-transparent placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded-md px-3 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm focus:shadow"
                        placeholder="Edit Name"
                        value={editingProduct.name}
                        onChange={(e) =>
                            setEditingProduct((prev) =>
                                prev ? {...prev, name: e.target.value} : prev
                            )
                        }
                    />
                    <input
                        type="number"
                        className="bg-transparent placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded-md px-3 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm focus:shadow"
                        placeholder="Edit Price"
                        value={editingProduct.price}
                        onChange={(e) =>
                            setEditingProduct((prev) =>
                                prev ? {...prev, price: Number(e.target.value)} : prev
                            )
                        }
                    />

                    <input
                        type="number"
                        className="bg-transparent placeholder:text-slate-400 text-slate-700 text-sm border border-slate-200 rounded-md px-3 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 shadow-sm focus:shadow"
                        placeholder="Edit Quantity"
                        value={editingProduct.quantity}
                        onChange={(e) =>
                            setEditingProduct((prev) =>
                                prev ? {...prev, quantity: Number(e.target.value)} : prev
                            )
                        }
                    />
                    <button
                        onClick={updateProduct}
                        className="rounded-md border border-slate-300 py-2 px-4 text-center text-sm transition-all shadow-sm hover:shadow-lg text-slate-600 hover:text-white hover:bg-slate-800 hover:border-slate-800 focus:text-white focus:bg-slate-800 focus:border-slate-800 active:border-slate-800 active:text-white active:bg-slate-800 disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none"

                    >
                        Update
                    </button>

                    <button
                        onClick={() => setEditingProduct(null)}
                        className="rounded-md border border-slate-300 py-2 px-4 text-center text-sm transition-all shadow-sm hover:shadow-lg text-slate-600 hover:text-white hover:bg-slate-800 hover:border-slate-800 focus:text-white focus:bg-slate-800 focus:border-slate-800 active:border-slate-800 active:text-white active:bg-slate-800 disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none"

                    >
                        Cancel
                    </button>
                </div>
            )}

            {/* List */}


            <div className="relative flex flex-col w-full text-gray-700 bg-white shadow-md rounded-xl bg-clip-border">
                <table className="w-full text-left table-auto min-w-max">
                    <thead>
                    <tr>
                        <th className="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                            <p className="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">
                                Name
                            </p>
                        </th>
                        <th className="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                            <p className="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">
                                Price
                            </p>
                        </th>

                        <th className="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                            <p className="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">
                                Quantity
                            </p>
                        </th>
                        <th className="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                            <p className="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">
                                Actions
                            </p>
                        </th>
                    </tr>
                    </thead>

                    <tbody>
                    {products.map((product, idx) => (
                        <tr key={product.id ?? `tmp-${idx}`}>
                            <td className="p-4 border-b border-blue-gray-50">
                                <p className="block font-sans text-sm antialiased font-normal leading-normal text-blue-gray-900">
                                    {product.name}
                                </p>
                            </td>
                            <td className="p-4 border-b border-blue-gray-50">
                                <p className="block font-sans text-sm antialiased font-normal leading-normal text-blue-gray-900">
                                    {product.price}
                                </p>
                            </td>
                            <td className="p-4 border-b border-blue-gray-50">
                                <p className="block font-sans text-sm antialiased font-normal leading-normal text-blue-gray-900">
                                    {product.quantity}
                                </p>
                            </td>

                            <td className="p-4 border-b border-blue-gray-50">
                                <div className="flex items-center gap-3">
                                    <button
                                        onClick={() => setEditingProduct(product)}
                                        className="px-3 py-1 rounded-lg text-sm font-medium hover:underline"
                                    >
                                        Edit
                                    </button>
                                    {product.id && (
                                        <button
                                            onClick={() => deleteProduct(product.id!)}
                                            className="px-3 py-1 rounded-lg text-sm font-medium hover:underline"
                                        >
                                            Delete
                                        </button>
                                    )}
                                </div>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>


        </div>
    );
};

export default CrudApp;
