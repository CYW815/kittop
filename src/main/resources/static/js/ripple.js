async function addRipple(rippleObj){
    const result = await axios.post(`/kittop/ripple/`, rippleObj);
    return result;
}

async function listRipple(boardId){
    const result = await axios.get(`/kittop/ripple/${boardId}`);
    return result;
}

async function removeRipple(rippldId){
    const result = await axios.delete(`/kittop/ripple/${rippldId}`);
    return result;
}