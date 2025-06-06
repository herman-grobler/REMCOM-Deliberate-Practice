export interface Notice {
    id: string;
    type: string;
    description: string;
    issueDate: string;
    status: string;
}

export interface Warrant {
    id: string;
    type: string;
    description: string;
    issueDate: string;
    status: string;
    issuingAuthority: string;
}

export interface LookupResult {
    identifier: string;
    type: 'DRIVER' | 'VEHICLE';
    notices: Notice[];
    warrants: Warrant[];
}
