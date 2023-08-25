async function loadPage(num){
    const result = await axios.get(`kittop/loadPage/${num}`);
    return result;
}