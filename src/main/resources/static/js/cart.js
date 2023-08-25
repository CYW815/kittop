async function addCart(itemId) {
    const result = await axios.get(`/kittop/addCart/${itemId}`)
    return result.data;
}

async function delCart(cartId){
    const result = await axios.delete(`/kittop/addCart/${cartId}`)
    return result.data;
}
