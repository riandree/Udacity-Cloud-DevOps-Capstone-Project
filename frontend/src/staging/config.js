// Default config for local dev (will be overwritten for prod while deploying frontend)

export default {
    baseURL : 'http://localhost:8080',
    amplifyAuth: {
        region: 'eu-central-1',
        userPoolId: 'eu-central-1_wr3AourNH',
        userPoolWebClientId: '6gi7km51i3jou67q1j5pdenavv'
    }
}