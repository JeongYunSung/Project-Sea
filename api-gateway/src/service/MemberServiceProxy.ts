import fetch from 'node-fetch'
import DataLoader from 'data-loader'

class MemberServiceProxy {

    private memberService: string;

    constructor(options) {
        this.memberService = `${options.baseUrl}/members`
    }
}

export default MemberServiceProxy;