async function payCart(itemObj) {
    const result = await axios.post(`/kittop/payCart/`, itemObj)
    return result;
}

async function delPayCart(rno){
    const result = await axios.delete(`/kittop/payCart/${rno}`);
    return result;
}

async function listCart(){
    const result = await axios.get(`/kittop/payCart/list`);
    return result.data;
}